package se.payerl.noted.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import se.payerl.noted.OverviewAdapter
import se.payerl.noted.R
import se.payerl.noted.model.Note
import se.payerl.noted.model.db.AppDatabase
import se.payerl.noted.model.db.NoteEntity
import se.payerl.noted.utils.DbHelp
import java.text.SimpleDateFormat
import java.util.*

class OverviewFragment : Fragment() {
    lateinit var fab_btn: FloatingActionButton
    lateinit var list: RecyclerView
    lateinit var adapter: OverviewAdapter
    lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)
        db = DbHelp.createInstance(requireContext().applicationContext)
        fab_btn = view.findViewById<FloatingActionButton>(R.id.fab_btn)
        list = view.findViewById<RecyclerView>(R.id.overview_list)
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = llm
        var notes: List<Note> = mutableListOf()
        db.queryExecutor.execute {
            notes = db.noteDao().getAll().map { it.toNote() }
            adapter = OverviewAdapter(notes, this.requireContext())
            list.adapter = adapter
            Log.w("Query", "finished ${notes.size}")
        }
//        adapter = OverviewAdapter(notes, this.requireContext())
//        list.adapter = adapter
        list.addItemDecoration(DividerItemDecoration(context, llm.orientation))

        fab_btn.setOnClickListener {
            val note = Note()
            note.name = SimpleDateFormat("dd/MM-yy HH:mm:ss").format(Date(System.currentTimeMillis()))
            db.queryExecutor.execute {
                db.noteDao().insertAll(note.toEntity<NoteEntity>())
            }
            adapter.addData(note)
        }
        return view
    }

    private fun alert() {
        context?.let { context ->
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)

            alertDialogBuilder.setTitle("What copy type?")
            alertDialogBuilder.setSingleChoiceItems(arrayOf<String>("Reference", "Value"), 1) { _,_ -> false }

            alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
                true
            }
            alertDialogBuilder.setNeutralButton("") { _,_ ->
                //TODO Explain reference vs value
                true }
            alertDialogBuilder.setNeutralButtonIcon(ResourcesCompat.getDrawable(resources, R.drawable.ic_help_outline_black_24, null))
            alertDialogBuilder.setPositiveButton("Select") { dialog, which ->
                (dialog as AlertDialog).listView.checkedItemPosition.let { pos ->
                    Toast.makeText(context, "Selected: $pos", Toast.LENGTH_SHORT).show()
                }
                true
            }

            alertDialogBuilder.create().show()
        }
    }
}