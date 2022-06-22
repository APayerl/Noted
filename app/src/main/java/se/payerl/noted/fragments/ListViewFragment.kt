package se.payerl.noted.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
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
//                    if(glAdapter.isModifiable.value == true) {
//                        EditRowPopup.open(requireContext(), _base) { base ->
//                            db.queryExecutor.execute {
//                                when(base.type) {
//                                    NoteType.LIST -> db.noteDao().update(m.noteToNoteEntity(base as Note))
//                                    NoteType.ROW_AMOUNT -> db.rowAmountDao().update(m.noteRowAmountToNoteRowAmountEntity(base as NoteRowAmount))
//                                    NoteType.ROW_TEXT -> db.rowTextDao().update(m.noteRowTextToNoteRowTextEntity(base as NoteRowText))
//                                }
//                            }
//                        }
//                    }
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
            findNavController().navigate(ListViewFragmentDirections.actionListViewFragmentSelf(base.uuid))
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

        (requireActivity() as MainActivity).let { main ->
            val uuid: String? = arguments?.getString("uuid")
            fab = setupFloatingActionButton(rootView, uuid)
            setToolbarTitle(uuid ?: "Noted")
            populateAdapter(uuid)
        }
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
            setOnClickListener {
                //TODO Fix add dialog for a new list?
                glAdapter.add(listOf(GeneralListAdapter.getRow(if(uuid == null) NoteType.LIST else defaultType, uuid)))
            }
            uuid?.let {
                setOnLongClickListener {
                    RowAddDialog.get(
                        this@ListViewFragment.requireContext(),
                        "What row type?",
                        NoteType.values().map { it.toString() }.toTypedArray(),
                        NoteType.values().indexOf(defaultType)
                    ) { chosen ->
                        glAdapter.add(
                            listOf(
                                GeneralListAdapter.getRow(
                                    NoteType.values()[chosen],
                                    uuid
                                )
                            )
                        )
                    }
                    true
                }
            }
        }
    }

    private fun setToolbarTitle(content: String) {
        (requireActivity() as MainActivity).let { main ->
            db.queryExecutor.execute {
                db.noteDao().findByUUID(content).let { entity ->
                    entity?.let { note = m.noteEntityToNote(it) }
                    val name = entity?.name
                    main.runOnUiThread {
                        main.toolbar.title = name ?: content
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
                list.addAll(
                    db.rowTextDao().findByParent(uuid)
                        .map { m.noteRowTextEntityToNoteRowText(it) })
                list.addAll(
                    db.rowAmountDao().findByParent(uuid)
                        .map { m.noteRowAmountEntityToNoteRowAmount(it) })
                list.addAll(db.noteDao().findByParent(uuid).map { m.noteEntityToNote(it) })
            }
            list.sortBy(NoteBase::createdAt)

            this@ListViewFragment.requireActivity().runOnUiThread {
                glAdapter.add(list)
            }
        }
    }
}