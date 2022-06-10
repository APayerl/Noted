package se.payerl.noted.model

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import se.payerl.noted.R
import se.payerl.noted.adapters.GeneralListAdapter

class RowTextVL(itemView: View, src: GeneralListAdapter.GeneralVH, gla: GeneralListAdapter): ViewLogic(itemView, src, gla) {
    var textView: TextView

    init {
        val subLayout = LayoutInflater.from(src.parent.context).inflate(R.layout.list_item_row_text, src.parent, false)
        textView = subLayout.findViewById<TextView>(R.id.list_item_row_text_content).apply {
            text = (src.data as NoteRowText).content
        }
        subLayoutFrame.addView(subLayout)
    }
}