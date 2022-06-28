package se.payerl.noted.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.appcompat.view.ActionMode
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import se.payerl.noted.MainActivity
import se.payerl.noted.R
import se.payerl.noted.adapters.GeneralListAdapter
import se.payerl.noted.model.*
import se.payerl.noted.model.db.AppDatabase
import se.payerl.noted.model.db.Mapper
import se.payerl.noted.utils.EditRowPopup
import se.payerl.noted.utils.MyDetailsLookup
import se.payerl.noted.utils.RowAddDialog
import javax.inject.Inject

@AndroidEntryPoint
class ListViewFragment : Fragment() {
    val args: ListViewFragmentArgs by navArgs()
    @Inject lateinit var db: AppDatabase
    lateinit var note: Note
    private val m: Mapper = Mapper()
    lateinit var fab: FloatingActionButton
    lateinit var recyclerView: RecyclerView
    private lateinit var glAdapter: GeneralListAdapter
    private lateinit var tracker: SelectionTracker<String>
    private val actionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.action_mode_menu, menu)
            return true
        }
        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }
        override fun onActionItemClicked(
            mode: ActionMode?,
            item: MenuItem
        ): Boolean {
            return when(item.itemId) {
                R.id.delete -> {
                    val mList: MutableSelection<String> = MutableSelection()
                    tracker.copySelection(mList)
                    mList.forEach { selection ->
                        glAdapter.getItemByUUID(selection)?.let { base ->
                            glAdapter.remove(base)
                        }
                    }
                    true
                }
                R.id.edit -> {
                    if(glAdapter.isModifiable.value == true) {
                        EditRowPopup.open(requireContext(), _base) { base ->
                            db.queryExecutor.execute {
                                when(base.type) {
                                    NoteType.LIST -> db.noteDao().update(m.noteToNoteEntity(base as Note))
                                    NoteType.ROW_AMOUNT -> db.rowAmountDao().update(m.noteRowAmountToNoteRowAmountEntity(base as NoteRowAmount))
                                    NoteType.ROW_TEXT -> db.rowTextDao().update(m.noteRowTextToNoteRowTextEntity(base as NoteRowText))
                                }
                            }
                        }
                    }
                    false
                }
                else -> false
            }
        }
        override fun onDestroyActionMode(mode: ActionMode?) {
            tracker.selection.forEach { glAdapter.getItemByUUID(it)?.selected?.postValue(false) }
            actionMode = null
        }
    }
    private var actionMode: ActionMode? = null
    private val rowShortClickListener: (base: NoteBase) -> Boolean = { base ->
        if(actionMode == null && base.type == NoteType.LIST) {
            val ids: Array<String>? = listOf<List<String?>?>(
                args.uuids?.asList(),
                listOf(base.uuid)
            )
            .filterNotNull()
            .flatten()
            .filterNotNull()
            .toTypedArray()
            findNavController().navigate(ListViewFragmentDirections.actionListViewFragmentSelf(ids))
            true
        } else false
    }
    private val rowLongClickListener: (base: NoteBase) -> Unit = { _base ->
        tracker.select(_base.uuid)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list_view, container, false)
        recyclerView = setupRecyclerView(rootView)
        tracker = setupSelectionTracker(recyclerView, glAdapter)

        fab = setupFloatingActionButton(rootView, args.uuids?.lastOrNull())
        populateAdapter(args.uuids?.lastOrNull())
        setToolbarTitle(args.uuids)
        return rootView
    }

    private fun setupRecyclerView(rootView: View): RecyclerView {
        return rootView.findViewById<RecyclerView>(R.id.list).apply {
            LinearLayoutManager(context).let { llm ->
                llm.orientation = LinearLayoutManager.VERTICAL
                layoutManager = llm
                addItemDecoration(DividerItemDecoration(context, llm.orientation))
            }
            glAdapter = GeneralListAdapter(db, rowShortClickListener, rowLongClickListener)
            adapter = glAdapter
        }
    }

    private fun setupSelectionTracker(recyclerView: RecyclerView, glAdapter: GeneralListAdapter): SelectionTracker<String> {
        return SelectionTracker.Builder<String>(
            "my-selection-id",
            recyclerView,
            object : ItemKeyProvider<String>(ItemKeyProvider.SCOPE_MAPPED) {
                override fun getKey(position: Int): String {
                    return glAdapter.dataList[position].uuid
                }

                override fun getPosition(key: String): Int {
                    return glAdapter.dataList.indexOfFirst { x -> x.uuid == key }
                }
            },
            MyDetailsLookup(recyclerView),
            StorageStrategy.createStringStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build().also {
            it.addObserver(object : SelectionTracker.SelectionObserver<String>() {
                override fun onItemStateChanged(key: String, selected: Boolean) {
                    glAdapter.getItemByUUID(key)?.selected?.postValue(selected)
                    if(actionMode == null) {
                        if(selected) actionMode = (activity as AppCompatActivity).startSupportActionMode(actionModeCallback)
                    } else {
                        actionMode!!.menu.findItem(R.id.edit).isVisible = tracker.selection.size() <= 1
                        if(!tracker.hasSelection()) actionMode?.finish()
                    }
                }
            })
        }
    }

    private fun setupFloatingActionButton(rootView: View, uuid: String?): FloatingActionButton {
        return rootView.findViewById<FloatingActionButton>(R.id.floatingActionButton).apply {
            val defaultType: NoteType = NoteType.valueOf(
                requireContext().getSharedPreferences("", Context.MODE_PRIVATE)
                    .getString("default_add_action", NoteType.ROW_TEXT.toString())!!
            )
            if(uuid == null) {
                setOnClickListener {
                    openListDialog() { glAdapter.add(it) }
                }
            } else {
                db.queryExecutor.execute {
                    db.noteDao().findByUUID(uuid)?.let {
                        val note = m.noteEntityToNote(it)
                        setOnClickListener {
                            glAdapter.add(GeneralListAdapter.getRow(defaultType, note))
                        }
                        setOnLongClickListener {
                            RowAddDialog.get(
                                this@ListViewFragment.requireContext(),
                                "What row type?",
                                NoteType.values().map { it.toString() }.toTypedArray(),
                                NoteType.values().indexOf(defaultType)
                            ) { chosen ->
                                if(NoteType.LIST.ordinal == chosen) {
                                    openListDialog(uuid) { glAdapter.add(it) }
                                } else glAdapter.add(GeneralListAdapter.getRow(
                                    NoteType.values()[chosen],
                                    note
                                ))
                            }
                            true
                        }
                    }
                }
            }
        }
    }

    private fun openListDialog(parent: String? = null, callback: (note: Note) -> Unit) {
        View.inflate(requireContext(), R.layout.new_list_dialog, null).let { view ->
            AlertDialog.Builder(requireContext())
                .setView(R.layout.new_list_dialog)
                .setPositiveButton("Insert") { dialogInterface, digit ->
                    callback(Note(parent = parent).apply {
                        name = (dialogInterface as AlertDialog).findViewById<EditText>(R.id.inputField)?.text.toString()
                    })
                }
                .create().apply {
                    setOnShowListener {
                        val positiveBtn = (it as AlertDialog).getButton(DialogInterface.BUTTON_POSITIVE).apply {
                            isEnabled = false
                        }
                        val inputField = (it as AlertDialog).findViewById<EditText>(R.id.inputField)?.apply {
                            addTextChangedListener {
                                it?.let {
                                    positiveBtn.isEnabled = it.isNotEmpty()
                                }
                            }
                        }
                    }
                }.show()
        }
    }

    private fun setToolbarTitle(content: Array<String>?) {
        (requireActivity() as MainActivity).let { main ->
            if(content == null) main.toolbar.title = "Noted"
            else {
                db.queryExecutor.execute {
                    val sb = StringBuilder()
                    content
                        .map { uuid -> db.noteDao().findByUUID(uuid)?.name }
                        .forEach { name -> sb.append("${if(sb.isNotEmpty()) " > " else ""}$name") }
                    main.runOnUiThread {
                        main.toolbar.title = sb.toString()
                    }
                }
            }
        }
    }

    private fun populateAdapter(uuid: String?) {
        db.queryExecutor.execute {
            val list = mutableListOf<NoteBase>()
            if (uuid == null) {
                list.addAll(db.noteDao().findRootLists().map { m.noteEntityToNote(it) })
            } else {
                list.addAll(db.mixedDao().getChildren(uuid).map { m.entityToBase(it) })
            }

            this@ListViewFragment.requireActivity().runOnUiThread {
                glAdapter.populate(list)
            }
        }
    }
}