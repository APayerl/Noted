package se.payerl.noted.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import se.payerl.noted.R
import se.payerl.noted.adapters.GeneralListAdapter
import se.payerl.noted.model.*
import se.payerl.noted.model.db.AppDatabase
import se.payerl.noted.model.db.Mapper
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
    private val rowClickListener: (base: NoteBase) -> Unit = { base ->
        Toast.makeText(this@ListViewFragment.requireContext(), "Clicked ${base.uuid}", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_list_view, container, false)
        recyclerView = rootView.findViewById<RecyclerView>(R.id.list).apply {
            LinearLayoutManager(context).let { llm ->
                llm.orientation = LinearLayoutManager.VERTICAL
                layoutManager = llm
                addItemDecoration(DividerItemDecoration(context, llm.orientation))
            }
            glAdapter = GeneralListAdapter(listOf(), db, rowClickListener)
            adapter = glAdapter
        }
        requireArguments().getString("uuid")?.let { uuid ->
            fab = rootView.findViewById<FloatingActionButton>(R.id.floatingActionButton).apply {
                val defaultType: NoteType = NoteType.valueOf(requireContext().getSharedPreferences("", Context.MODE_PRIVATE).getString("default_add_action", NoteType.ROW_TEXT.toString())!!)
                setOnClickListener {
                    glAdapter.addToList(GeneralListAdapter.getRow(defaultType, uuid))
                }
                setOnLongClickListener {
                    RowAddDialog.get(this@ListViewFragment.requireContext(),
                        "What row type?",
                        NoteType.values().map{it.toString()}.toTypedArray(),
                        NoteType.values().indexOf(defaultType)) { chosen ->
                        glAdapter.addToList(GeneralListAdapter.getRow(NoteType.values()[chosen], uuid))
                    }
                    true
                }
            }
            db.queryExecutor.execute {
                val list = listOf(
                    db.rowTextDao().findByParent(uuid).map { m.noteRowTextEntityToNoteRowText(it) },
                    db.rowAmountDao().findByParent(uuid).map { m.noteRowAmountEntityToNoteRowAmount(it) },
                    db.noteDao().findByParent(uuid).map { m.noteEntityToNote(it) }).flatten()

                this@ListViewFragment.requireActivity().runOnUiThread {
                    glAdapter.populate(list)
                }
            }
            rootView.findViewById<TextView>(R.id.child)?.apply {
                text = uuid
            }
        }
        return rootView
    }
}