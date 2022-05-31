package se.payerl.noted.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import se.payerl.noted.OverviewAdapter
import se.payerl.noted.R
import se.payerl.noted.model.Note
import java.text.SimpleDateFormat
import java.util.*

class OverviewFragment : Fragment() {
//    lateinit var bar: BottomAppBar
    lateinit var fab_btn: FloatingActionButton
    lateinit var list: RecyclerView
    lateinit var adapter: OverviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)
//        bar = view.findViewById<BottomAppBar>(R.id.bottomAppBar)
        fab_btn = view.findViewById<FloatingActionButton>(R.id.fab_btn)
        list = view.findViewById<RecyclerView>(R.id.overview_list)
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = llm
        adapter = OverviewAdapter(listOf(), this.requireContext())
        list.adapter = adapter
        list.addItemDecoration(DividerItemDecoration(context, llm.orientation))
//        bar.setOnMenuItemClickListener {
//            when(it.itemId) {
//                R.id.about -> findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToAboutFragment())
//                R.id.more -> "more"
//                R.id.search -> "search"
//                else -> "what???"
//            }
//            true
//        }

//        activity?.let {
//            val drawer = it.findViewById<DrawerLayout>(R.id.drawer_layout)
//            bar.setNavigationOnClickListener {
//                if(drawer.isOpen) drawer.close() else drawer.open()
//            }
//        }

        fab_btn.setOnClickListener {
            val note = Note()
            note.name = SimpleDateFormat("dd/MM-yy HH:mm:ss").format(Date(System.currentTimeMillis()))
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