package se.payerl.noted.model

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import se.payerl.noted.R
import se.payerl.noted.adapters.GeneralListAdapter

class RowNoteVL(itemView: View, src: GeneralListAdapter.GeneralVH, val shortListener: (note: NoteBase) -> Unit, val longListener: (note: NoteBase) -> Unit): ViewLogic(itemView) {
    private var name: TextView
    private var description: TextView
    private val content: Note

    init {
        LayoutInflater.from(src.parent.context).inflate(R.layout.list_item_note, src.parent, false).let { sub ->
            content = src.data as Note
            name = sub.findViewById<TextView>(R.id.item_name).apply {
                text = content.name
            }
            description = sub.findViewById<TextView>(R.id.item_description).apply {
                text = content.type.toString()
            }
            subLayoutFrame.addView(sub)
        }
    }

    override fun onFastClick(v: View): Boolean {
        shortListener(content)
        return true
    }

    override fun onLongClick(v: View): Boolean {
        longListener(content)
        return true
    }
}