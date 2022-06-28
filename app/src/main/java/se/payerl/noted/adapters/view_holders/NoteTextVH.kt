package se.payerl.noted.adapters.view_holders

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import se.payerl.noted.R
import se.payerl.noted.adapters.GeneralListAdapter
import se.payerl.noted.model.*
import se.payerl.noted.model.db.Mapper

class NoteTextVH(gla: GeneralListAdapter, itemView: View, parent: ViewGroup, val shortListener: (note: NoteRowText) -> Boolean, longListener: (note: NoteRowText) -> Unit): GeneralVH<NoteRowText>(gla, itemView, parent, longListener) {
    override val checkboxChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if(data.done != isChecked) {
            data.done = isChecked
            gla.db.queryExecutor.execute {
                gla.db.rowTextDao().update(Mapper().noteRowTextToNoteRowTextEntity(data))
            }
        }
    }
    override lateinit var data: NoteRowText
    private val content: TextView

    init {
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_row_text, parent, false).let { sub ->
            content = sub.findViewById<TextView>(R.id.list_item_row_text_content)
            subLayoutFrame.addView(sub)
        }
    }

    override fun initialize(_d: NoteRowText) {
        data = _d

        data.selected.observeForever { isSelected ->
            Log.w("observed", "${data.content}: $isSelected")
            selectionOverlay.visibility = if(isSelected) View.VISIBLE else View.GONE
        }

        gla.isDeletable.observeForever {
            trash.visibility = if(it) ImageView.VISIBLE else ImageView.GONE
        }
        checkbox.setOnCheckedChangeListener(checkboxChangeListener)
        content.apply {
            text = data.content
        }
    }

    override fun onFastClick(v: View): Boolean {
        return shortListener(data)
    }
}