package se.payerl.noted.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import se.payerl.noted.R
import se.payerl.noted.model.*
import se.payerl.noted.model.db.*
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class GeneralListAdapter(val db: AppDatabase, val fastListener: (note: NoteBase) -> Unit, val longListener: (note: NoteBase) -> Unit, _d: List<NoteBase> = listOf()) : RecyclerView.Adapter<GeneralListAdapter.GeneralVH>() {
    var isDeletable: LiveData<Boolean> = MutableLiveData<Boolean>(false)
        private set
    var isModifiable: LiveData<Boolean> = MutableLiveData<Boolean>(true)
        private set
    private val _data: MutableList<NoteBase> = _d.toMutableList()

    inner class GeneralVH(private val itemView: View, val parent: ViewGroup): RecyclerView.ViewHolder(itemView) {
        lateinit var data: NoteBase
        private lateinit var logic: ViewLogic
        val checkbox: CheckBox = itemView.findViewById<CheckBox>(R.id.general_list_checkbox)
        val trash: ImageView = itemView.findViewById<ImageView>(R.id.item_delete_btn)

        fun initialize(_d: NoteBase) {
            data = _d

            isDeletable.observeForever {
                trash.visibility = if(it) ImageView.VISIBLE else ImageView.GONE
            }
            trash.setOnClickListener {
                val index = _data.indexOf(data)
                _data.remove(data)
                notifyItemRemoved(index)
            }

            logic = when(data.type) {
                NoteType.ROW_TEXT -> RowTextVL(itemView, this, fastListener, longListener)
                NoteType.ROW_AMOUNT -> RowAmountVL(itemView, this, fastListener, longListener)
                NoteType.LIST -> RowNoteVL(itemView, this, fastListener, longListener)
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

    fun populate(content: List<NoteBase>) {
        val x = _data.size
        _data.addAll(x, content)
        _data.sortBy(NoteBase::createdAt)
        notifyDataSetChanged()
    }

    fun addToList(content: NoteBase) {
        _data.add(content)
        persistNote(content)
        notifyItemInserted(_data.size - 1)
    }

    private fun persistNote(base: NoteBase) {
        db.queryExecutor.execute {
            val m = Mapper()

            when(base.type) {
                NoteType.LIST -> {
                    val dao = db.noteDao()
                    val entity: NoteEntity = m.noteToNoteEntity(base as Note)
                    if(dao.hasUUID(base.uuid)) {
                        dao.update(entity)
                    } else dao.insert(entity)
                }
                NoteType.ROW_AMOUNT -> {
                    val dao = db.rowAmountDao()
                    val entity: NoteRowAmountEntity = m.noteRowAmountToNoteRowAmountEntity(base as NoteRowAmount)
                    if(dao.hasUUID(base.uuid)) {
                        dao.update(entity)
                    } else dao.insert(entity)
                }
                NoteType.ROW_TEXT -> {
                    val dao = db.rowTextDao()
                    val entity: NoteRowTextEntity = m.noteRowTextToNoteRowTextEntity(base as NoteRowText)
                    if(dao.hasUUID(base.uuid)) {
                        dao.update(entity)
                    } else dao.insert(entity)
                    m.noteRowTextToNoteRowTextEntity(base)
                }
            }
        }
    }

    companion object {
        fun getRow(row: NoteType, uuid: String?): NoteBase {
            return when(row) {
                NoteType.ROW_TEXT -> NoteRowText(parent = uuid).apply { content = createdAt.toInstant(ZoneOffset.UTC).toEpochMilli().toString() }
                NoteType.ROW_AMOUNT -> NoteRowAmount(parent = uuid).apply { content = createdAt.toInstant(ZoneOffset.UTC).toEpochMilli().toString() }
                NoteType.LIST -> Note(parent = uuid).apply { name = createdAt.format(DateTimeFormatter.ofPattern("dd/MM-yy HH:mm:ss")) }
            }
        }
    }
}