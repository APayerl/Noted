package se.payerl.noted.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import se.payerl.noted.model.NoteType
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "row_amount")
class NoteRowAmountEntity: se.payerl.noted.model.db.Entity {
    @ColumnInfo(name = "parent")
    override var parent: String? = null

    @ColumnInfo(name = "content")
    var content: String = ""

    @ColumnInfo(name = "amount_when_finished")
    var amountWhenFinished: Int = 0

    @ColumnInfo(name = "amount")
    var amount: Int = 1

    @PrimaryKey
    @ColumnInfo(name = "uuid")
    override var uuid: String = ""

    @ColumnInfo(name = "type")
    override var type: NoteType = NoteType.ROW_AMOUNT

    @ColumnInfo(name = "created_at")
    override var createdAt: LocalDateTime = LocalDateTime.now()
}