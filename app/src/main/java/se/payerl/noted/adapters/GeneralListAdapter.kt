package se.payerl.noted.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import se.payerl.noted.R
import se.payerl.noted.model.*
import se.payerl.noted.model.db.AppDatabase
import java.time.ZoneOffset

class GeneralListAdapter(_d: List<NoteBase>, val db: AppDatabase, val listener: (note: NoteBase) -> Unit) : RecyclerView.Adapter<GeneralListAdapter.GeneralVH>() {
    private val _data: MutableList<NoteBase> = _d.toMutableList()

    inner class GeneralVH(val itemView: View, val parent: ViewGroup): RecyclerView.ViewHolder(itemView) {
        lateinit var data: NoteBase
        private lateinit var logic: ViewLogic
        val checkbox: CheckBox = itemView.findViewById<CheckBox>(R.id.general_list_checkbox)
        val trash: ImageView = itemView.findViewById<ImageView>(R.id.item_delete_btn)

        fun initialize(_d: NoteBase) {
            data = _d

            trash.setOnClickListener {
                parent.removeView(itemView)
            }

            logic = when(data.type) {
                NoteType.ROW_TEXT -> RowTextVL(itemView, this, this@GeneralListAdapter)
                NoteType.ROW_AMOUNT -> RowAmountVL(itemView, this, this@GeneralListAdapter)
                else -> RowNoteVL(itemView, this, this@GeneralListAdapter)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneralVH {
        return GeneralVH(LayoutInflater.from(parent.context).inflate(R.layout.general_list_layout, parent, false), parent)
    }

    override fun onBindViewHolder(holder: GeneralVH, position: Int) {
        holder.initialize(_data[position])
    }

    override fun getItemCount(): Int {
        return _data.size
    }

    public fun populate(content: List<NoteBase>) {
        val x = _data.size
        _data.addAll(x, content)
        _data.sortBy(NoteBase::createdAt)
        notifyDataSetChanged()
    }

    fun addToList(content: NoteBase) {
        _data.add(content)
        notifyItemInserted(_data.size - 1)
    }

    companion object {
        fun getRow(row: NoteType, uuid: String): NoteBase {
            return when(row) {
                NoteType.ROW_TEXT -> NoteRowText(parent = uuid).apply { content = createdAt.toInstant(ZoneOffset.UTC).toEpochMilli().toString() }
                else -> NoteRowAmount(parent = uuid).apply { content = createdAt.toInstant(ZoneOffset.UTC).toEpochMilli().toString() }
            }
        }
    }
}