package se.payerl.noted.model

import android.util.Log
import se.payerl.noted.model.db.*
import se.payerl.noted.utils.AddListener
import se.payerl.noted.utils.ListenableList
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

class Note constructor(
    override val uuid: String = UUID.randomUUID().toString(),
    override val type: NoteType = NoteType.LIST,
    override val createdAt: LocalDateTime = LocalDateTime.now()
) : NoteBase {
    val m: Mapper = Mapper()
    @Inject lateinit var db: AppDatabase

    var name: String = ""
    var content: ListenableList<NoteBase> = ListenableList(mutableListOf(), object : AddListener<NoteBase> {
        override fun add(element: NoteBase) {
            Log.i("Note", "Note ${element.uuid} added.")

            when (type) {
                NoteType.ROW_TEXT -> db.rowTextDao().insertAll(m.noteRowTextToNoteRowTextEntity(element as NoteRowText))
                else -> db.rowAmountDao().insertAll(m.noteRowAmountToNoteRowAmountEntity(element as NoteRowAmount))
            }
        }
    })
}