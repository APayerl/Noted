package se.payerl.noted.fragments

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
import se.payerl.noted.model.Note
import se.payerl.noted.model.NoteBase
import se.payerl.noted.model.NoteRowText
import se.payerl.noted.model.db.AppDatabase
import se.payerl.noted.model.db.Mapper
import java.time.LocalDateTime
import java.time.ZoneOffset
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
                setOnClickListener {
                    glAdapter.addToList(NoteRowText(uuid).apply { content = createdAt.toInstant(ZoneOffset.UTC).toEpochMilli().toString() })
                }
            }
            db.queryExecutor.execute {
                glAdapter.populate(db.rowTextDao().findByParent(uuid).map { m.noteRowTextEntityToNoteRowText(it) })
                glAdapter.populate(db.rowAmountDao().findByParent(uuid).map { m.noteRowAmountEntityToNoteRowAmount(it) })
                glAdapter.populate(db.noteDao().findByParent(uuid).map { m.noteEntityToNote(it) })
            }
            rootView.findViewById<TextView>(R.id.child)?.apply {
                text = uuid
            }
        }
        return rootView
    }
}