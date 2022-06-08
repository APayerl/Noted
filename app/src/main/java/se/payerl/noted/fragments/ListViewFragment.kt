package se.payerl.noted.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import se.payerl.noted.R
import se.payerl.noted.model.Note
import se.payerl.noted.model.NoteRow
import se.payerl.noted.model.db.AppDatabase
import se.payerl.noted.model.db.Mapper
import javax.inject.Inject

class ListViewFragment : Fragment() {
    @Inject lateinit var db: AppDatabase
    lateinit var note: Note
    val m: Mapper = Mapper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_view, container, false).apply {
            arguments?.let { args ->
                args.getString("uuid")?.let { uuid ->
                    db.queryExecutor.execute {
                        val mList = mutableListOf<NoteRow>()
                        db.rowTextDao().findByParent(uuid).map { mList.add(m.noteRowTextEntityToNoteRowText(it)) }
                        db.rowAmountDao().findByParent(uuid).map { mList.add(m.noteRowAmountEntityToNoteRowAmount(it)) }
                        mList.sortBy(NoteRow::createdAt)
                    }
                    findViewById<TextView>(R.id.child)?.apply {
                        text = uuid
                    }
                }
            }
        }
    }
}