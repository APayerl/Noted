package se.payerl.noted

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.DrawableWrapper
import android.graphics.drawable.Icon
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.DrawableUtils
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.BitmapCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.ExecutorCompat
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import se.payerl.noted.model.Note
import se.payerl.noted.model.NoteType
import se.payerl.noted.model.db.NoteEntity
import se.payerl.noted.utils.DbHelp

class OverviewAdapter(data: List<Note>, val context: Context): RecyclerView.Adapter<OverviewAdapter.OverviewVH>() {
    var _data: MutableList<Note> = data.toMutableList()
        private set(value) {
            field = value
        }

    inner class OverviewVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemName: TextView
        val itemDescription: TextView
        val itemButtons: LinearLayoutCompat
        val itemDeteleBtn: ImageView
        lateinit var note: Note

        init {
            itemName = itemView.findViewById<TextView>(R.id.item_name)
            itemDescription = itemView.findViewById<TextView>(R.id.item_description)
            itemButtons = itemView.findViewById<LinearLayoutCompat>(R.id.item_buttons)
            itemDeteleBtn = itemView.findViewById<ImageView>(R.id.item_delete_btn)
        }
    }

    override fun getItemCount(): Int {
        return _data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.overview_item_layout, parent, false)
        return OverviewVH(view)
    }

    override fun onBindViewHolder(holder: OverviewVH, position: Int) {
        holder.note = _data.get(position)
        holder.itemName.text = holder.note.name
        holder.itemDescription.text = holder.note.type.name
        holder.itemDeteleBtn.setOnClickListener {
            DbHelp.get()?.queryExecutor?.execute {
                when(holder.note.type) {
                    NoteType.LIST -> DbHelp.get()?.let { db ->
                        db.noteDao().delete(holder.note.toEntity<NoteEntity>())
                    }
                }
            }
            val index = _data.indexOf(holder.note)
            _data.remove(holder.note)
            notifyItemRemoved(index)
        }
    }

    fun addData(note: Note): OverviewAdapter {
        _data.add(note)
        notifyItemInserted(_data.size - 1)
        return this
    }
}