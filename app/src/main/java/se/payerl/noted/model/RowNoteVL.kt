package se.payerl.noted.model

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import se.payerl.noted.R
import se.payerl.noted.adapters.GeneralListAdapter

class RowNoteVL(itemView: View, src: GeneralListAdapter.GeneralVH, gla: GeneralListAdapter): ViewLogic(itemView, src, gla) {
    private var name: TextView
    private var description: TextView

    init {
        LayoutInflater.from(src.parent.context).inflate(R.layout.list_item_note, src.parent, false).let { sub ->
            val content: Note = src.data as Note
            name = sub.findViewById<TextView>(R.id.item_name).apply {
                text = content.name
            }
            description = sub.findViewById<TextView>(R.id.item_description).apply {
                text = content.type.toString()
            }
            subLayoutFrame.addView(sub)
        }
    }
}