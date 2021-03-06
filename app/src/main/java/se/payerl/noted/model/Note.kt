package se.payerl.noted.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import se.payerl.noted.model.db.*
import se.payerl.noted.utils.AddListener
import se.payerl.noted.utils.ListenableList
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

class Note(
    override val uuid: String = UUID.randomUUID().toString(),
    override val type: NoteType = NoteType.LIST,
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    override var parent: String? = null,
    var name: String = "",
    override var selected: MutableLiveData<Boolean> = MutableLiveData(false)
) : NoteBase {
    private val m: Mapper = Mapper()
    //@Inject lateinit var db: AppDatabase

    var content: MutableList<NoteBase> = mutableListOf()
//    var content: ListenableList<NoteBase> = ListenableList(mutableListOf(), object : AddListener<NoteBase> {
//        override fun add(element: NoteBase) {
//            Log.i("Note", "Note ${element.uuid} added.")
//
//            when (type) {
//                NoteType.ROW_TEXT -> db.rowTextDao().insertAll(m.noteRowTextToNoteRowTextEntity(element as NoteRowText))
//                NoteType.ROW_AMOUNT -> db.rowAmountDao().insertAll(m.noteRowAmountToNoteRowAmountEntity(element as NoteRowAmount))
//                NoteType.LIST -> db.noteDao().insertAll(m.noteToNoteEntity(element as Note))
//            }
//        }
//    })

    override fun isDone(): Boolean {
        return content.none { it.isDone() }
    }
}