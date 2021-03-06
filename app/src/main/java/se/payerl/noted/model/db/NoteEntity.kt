package se.payerl.noted.model.db

import androidx.room.*
import androidx.room.Entity
import se.payerl.noted.model.Note
import se.payerl.noted.model.NoteType
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@Entity(tableName = "note")
class NoteEntity: se.payerl.noted.model.db.Entity {
    @ColumnInfo(name = "note_name")
    var name: String = ""

    @PrimaryKey
    @ColumnInfo(name = "uuid")
    override var uuid: String = ""

    @ColumnInfo(name = "type")
    override var type: NoteType = NoteType.LIST

    @ColumnInfo(name = "created_at")
    override var createdAt: LocalDateTime = LocalDateTime.now()

    @ColumnInfo(name = "parent")
    override var parent: String? = null
}