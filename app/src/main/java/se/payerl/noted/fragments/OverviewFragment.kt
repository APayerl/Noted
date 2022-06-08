package se.payerl.noted.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import se.payerl.noted.NavGraphDirections
import se.payerl.noted.adapters.OverviewAdapter
import se.payerl.noted.R
import se.payerl.noted.model.Note
import se.payerl.noted.model.db.AppDatabase
import se.payerl.noted.model.db.Mapper
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class OverviewFragment : Fragment() {
    lateinit var fab_btn: FloatingActionButton
    lateinit var list: RecyclerView
    lateinit var listAdapter: OverviewAdapter
    @Inject lateinit var db: AppDatabase
    val m: Mapper = Mapper()
    private val addNoteListener: View.OnClickListener = View.OnClickListener {
        val note = Note()
        note.name = SimpleDateFormat("dd/MM-yy HH:mm:ss").format(Date(System.currentTimeMillis()))
        listAdapter.addData(note)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)
        fab_btn = view.findViewById<FloatingActionButton>(R.id.fab_btn).apply {
            setOnClickListener(addNoteListener)
        }
        list = view.findViewById<RecyclerView>(R.id.overview_list).apply {
            LinearLayoutManager(context).let { llm ->
                llm.orientation = LinearLayoutManager.VERTICAL
                layoutManager = llm
                addItemDecoration(DividerItemDecoration(context, llm.orientation))
            }
            db.queryExecutor.execute {
                listAdapter = OverviewAdapter(db.noteDao().getAll().map { noteEntity -> m.noteEntityToNote(noteEntity) }, db) { note ->
                    Toast.makeText(activity, note.name, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToListViewFragment(note.uuid))
                }
                adapter = listAdapter
            }
            setOnClickListener {
                Log.w("click", "out row")
            }
        }
        return view
    }

//    private fun alert() {
//        context?.let { context ->
//            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
//
//            alertDialogBuilder.setTitle("What copy type?")
//            alertDialogBuilder.setSingleChoiceItems(arrayOf<String>("Reference", "Value"), 1) { _,_ -> false }
//
//            alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
//                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show()
//                true
//            }
//            alertDialogBuilder.setNeutralButton("") { _,_ ->
//                //TODO Explain reference vs value
//                true }
//            alertDialogBuilder.setNeutralButtonIcon(ResourcesCompat.getDrawable(resources, R.drawable.ic_help_outline_black_24, null))
//            alertDialogBuilder.setPositiveButton("Select") { dialog, which ->
//                (dialog as AlertDialog).listView.checkedItemPosition.let { pos ->
//                    Toast.makeText(context, "Selected: $pos", Toast.LENGTH_SHORT).show()
//                }
//                true
//            }
//
//            alertDialogBuilder.create().show()
//        }
//    }
}