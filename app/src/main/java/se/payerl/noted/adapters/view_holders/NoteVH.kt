package se.payerl.noted.adapters.view_holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import se.payerl.noted.R
import se.payerl.noted.adapters.GeneralListAdapter
import se.payerl.noted.model.*

class NoteVH(gla: GeneralListAdapter, itemView: View, parent: ViewGroup, val shortListener: (note: Note) -> Boolean, longListener: (note: Note) -> Unit): GeneralVH<Note>(gla, itemView, parent, longListener) {
    override lateinit var data: Note
    private lateinit var name: TextView
    private lateinit var description: TextView

    init {
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_note, parent, false).let { sub ->
            name = sub.findViewById<TextView>(R.id.item_name)
            description = sub.findViewById<TextView>(R.id.item_description)
            subLayoutFrame.addView(sub)
        }
    }

    override fun initialize(_d: Note) {
        data = _d

        data.selected.observeForever { isSelected ->
            selectionOverlay.visibility = if(isSelected) View.VISIBLE else View.GONE
        }

        gla.isDeletable.observeForever {
            trash.visibility = if(it) ImageView.VISIBLE else ImageView.GONE
        }
        if(data.parent != null) {
            checkbox.isChecked = data.isDone()
        } else checkbox.visibility = View.GONE

        checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            gla.sort()
        }
        trash.setOnClickListener {
            val index = gla.dataList.indexOf(data)
            gla.dataList.remove(data)
            gla.notifyItemRemoved(index)
        }
        name.text = data.name
        description.text = data.type.toString()
    }

    override fun onFastClick(v: View): Boolean {
        return shortListener(data)
    }
}