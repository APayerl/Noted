package se.payerl.noted.model

import java.time.LocalDateTime
import java.util.*

class NoteRowAmount(
    override var parent: String?,
    var content: String = "",
    var amountDone: Int = 0,
    var amount: Int = 1
) : NoteBase {
    override var uuid: String = UUID.randomUUID().toString()
    override var type: NoteType = NoteType.ROW_AMOUNT
    override var createdAt: LocalDateTime = LocalDateTime.now()

    override fun hasParent(): Boolean {
        return parent != null
    }
}