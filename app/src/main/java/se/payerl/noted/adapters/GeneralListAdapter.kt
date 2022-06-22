package se.payerl.noted.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import se.payerl.noted.R
import se.payerl.noted.adapters.view_holders.GeneralVH
import se.payerl.noted.adapters.view_holders.NoteAmountVH
import se.payerl.noted.adapters.view_holders.NoteTextVH
import se.payerl.noted.adapters.view_holders.NoteVH
import se.payerl.noted.model.*
import se.payerl.noted.model.db.*
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class GeneralListAdapter(val db: AppDatabase, val fastListener: (note: NoteBase) -> Boolean, val longListener: (note: NoteBase) -> Unit, _d: List<NoteBase> = listOf()) : RecyclerView.Adapter<GeneralVH<NoteBase>>() {
    var isDeletable: LiveData<Boolean> = MutableLiveData<Boolean>(false)
        private set
    var isModifiable: LiveData<Boolean> = MutableLiveData<Boolean>(true)
        private set
    val dataList: MutableList<NoteBase> = _d.toMutableList()
    val sortOrderCheckedOrNotAndThenCreatedAt = Comparator<NoteBase> { o1, o2 ->
        if(o1.isDone() && o2.isDone()) 0
        else if(o1.isDone()) -1
        else 1
    }

    init {
        setHasStableIds(false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): se.payerl.noted.adapters.view_holders.GeneralVH<NoteBase> {
        Log.v("ViewHolder type", "$viewType")
        return when(NoteType.values()[viewType]) {
            NoteType.LIST -> NoteVH(this, LayoutInflater.from(parent.context).inflate(R.layout.general_list_layout, parent, false), parent, fastListener, longListener)
            NoteType.ROW_AMOUNT -> NoteAmountVH(this, LayoutInflater.from(parent.context).inflate(R.layout.general_list_layout, parent, false), parent, fastListener, longListener)
            NoteType.ROW_TEXT -> NoteTextVH(this, LayoutInflater.from(parent.context).inflate(R.layout.general_list_layout, parent, false), parent, fastListener, longListener)
        } as se.payerl.noted.adapters.view_holders.GeneralVH<NoteBase>
    }

    override fun onBindViewHolder(holder: se.payerl.noted.adapters.view_holders.GeneralVH<NoteBase>, position: Int) {
        holder.initialize(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return dataList[position].type.ordinal
    }

    fun getItemByUUID(uuid: String): NoteBase? {
        return dataList.find { base -> base.uuid == uuid }
    }

    fun add(content: Collection<NoteBase>) {
        content.forEach { base ->
            dataList.add(base)
            if(content.size == 1) {
                persistNote(base)
                dataList.sortWith(sortOrderCheckedOrNotAndThenCreatedAt)
                notifyDataSetChanged()
            } else {
                notifyItemInserted(dataList.size - 1)
            }
        }
    }

    fun remove(content: NoteBase): Boolean {
        var result = false
        var index = dataList.indexOf(content)
        val m = Mapper()
        when(content.type) {
            NoteType.ROW_TEXT -> {
                db.queryExecutor.execute {
                    db.rowTextDao()
                        .delete(m.noteRowTextToNoteRowTextEntity(content as NoteRowText))
                }
                dataList.removeAt(index)
                notifyItemRemoved(index)
                result = true
            }
            NoteType.ROW_AMOUNT -> {
                db.queryExecutor.execute {
                    db.rowAmountDao()
                        .delete(m.noteRowAmountToNoteRowAmountEntity(content as NoteRowAmount))
                }
                dataList.removeAt(index)
                notifyItemRemoved(index)
                result = true
            }
        }
        return result
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