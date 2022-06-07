package se.payerl.noted

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import se.payerl.noted.model.Note
import se.payerl.noted.model.NoteType
import se.payerl.noted.model.db.AppDatabase
import se.payerl.noted.model.db.Mapper
import javax.inject.Inject

class OverviewAdapter(data: List<Note>, val db: AppDatabase): RecyclerView.Adapter<OverviewAdapter.OverviewVH>() {
    @Inject lateinit var _db: AppDatabase
    val m: Mapper = Mapper()

    var _data: MutableList<Note> = data.toMutableList()
        private set(value) {
            field = value
        }

    inner class OverviewVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemRow: ConstraintLayout
        val itemName: TextView
        val itemDescription: TextView
        val itemButtons: LinearLayoutCompat
        val itemDeteleBtn: ImageView
        lateinit var note: Note

        init {
            itemRow = itemView.findViewById<ConstraintLayout>(R.id.item_row)
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
            db.queryExecutor.execute {
                when(holder.note.type) {
                    NoteType.LIST -> db.noteDao().delete(m.noteToNoteEntity(holder.note))
                }
            }
            val index = _data.indexOf(holder.note)
            _data.remove(holder.note)
            notifyItemRemoved(index)
        }
        holder.itemView.setOnClickListener {
            Log.w("click", "Clicked on THE view!")
        }
    }

    fun addData(note: Note): OverviewAdapter {
        _data.add(note)
        persistNote(note)
        notifyItemInserted(_data.size - 1)
        return this
    }

    private fun persistNote(note: Note) {
        db.queryExecutor.execute {
            if(db.noteDao().hasUUID(note.uuid)) {
                db.noteDao().update(m.noteToNoteEntity(note))
            } else db.noteDao().insertAll(m.noteToNoteEntity(note))
        }
    }
}