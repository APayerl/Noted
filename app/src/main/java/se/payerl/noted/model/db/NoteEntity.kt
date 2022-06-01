package se.payerl.noted.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import se.payerl.noted.model.Note
import se.payerl.noted.model.NoteType
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "note")
class NoteEntity: se.payerl.noted.model.db.Entity {
    @ColumnInfo(name = "note_name")
    var name: String = ""

    @PrimaryKey
    @ColumnInfo(name = "uuid")
    var uuid: String = UUID.randomUUID().toString()

    @ColumnInfo(name = "type")
    var type: NoteType = NoteType.LIST

    @ColumnInfo(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()

    fun toNote(): Note {
        val note = Note(this.uuid, this.type, this.createdAt)
        note.name = this.name
        return note
    }
}