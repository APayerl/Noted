package se.payerl.noted.model

import android.util.JsonWriter
import se.payerl.noted.model.db.AppDatabase
import se.payerl.noted.model.db.NoteRowTextEntity
import se.payerl.noted.utils.DbHelp
import java.time.LocalDateTime
import java.util.*

class NoteRowText(
    val owner: String,
    var content: String = "",
    var done: Boolean = false) : NoteBase {
    override var uuid: String = UUID.randomUUID().toString()
    override var type: NoteType = NoteType.ROW_TEXT
    override var createdAt: LocalDateTime = LocalDateTime.now()

    override fun <T> toEntity(): NoteRowTextEntity {
        val entity = (DbHelp.get() as AppDatabase).rowTextDao().findByUUID(this.uuid)
        entity.content = this.content
        entity.owner = this.owner
        entity.done = this.done
        return entity
    }
}