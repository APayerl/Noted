package se.payerl.noted.model

import se.payerl.noted.model.db.AppDatabase
import se.payerl.noted.model.db.NoteRowAmountEntity
import se.payerl.noted.utils.DbHelp
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

    override fun <T> toEntity(): NoteRowAmountEntity {
        val entity = (DbHelp.get() as AppDatabase).rowAmountDao().findByUUID(this.uuid)
        entity.amount = this.amount
        entity.amountDone = this.amountDone
        entity.content = this.content
        return entity
    }
}