package se.payerl.noted.model

import se.payerl.noted.model.db.AppDatabase
import se.payerl.noted.model.db.NoteRowAmountEntity
import java.time.LocalDateTime
import java.util.*

class NoteRowAmount(
    var content: String = "",
    var amountDone: Int = 0,
    var amount: Int = 1
) : NoteBase {
    override var uuid: String = UUID.randomUUID().toString()
    override var type: NoteType = NoteType.ROW_AMOUNT
    override var createdAt: LocalDateTime = LocalDateTime.now()
}