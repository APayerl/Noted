package se.payerl.noted.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import se.payerl.noted.model.NoteType
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "row_amount")
class NoteRowAmountEntity: se.payerl.noted.model.db.Entity {
    @ColumnInfo(name = "content")
    var content: String = ""

    @ColumnInfo(name = "amount_done")
    var amountDone: Int = 0

    @ColumnInfo(name = "amount")
    var amount: Int = 1

    @PrimaryKey
    @ColumnInfo(name = "uuid")
    var uuid: String = ""

    @ColumnInfo(name = "type")
    var type: NoteType = NoteType.ROW_AMOUNT

    @ColumnInfo(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.now()
}