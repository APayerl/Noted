package se.payerl.noted.model

import java.time.LocalDateTime
import java.util.*

class NoteRowText(
    val owner: String,
    var content: String = "",
    var done: Boolean = false) : NoteBase {
    override var uuid: String = UUID.randomUUID().toString()
    override var type: NoteType = NoteType.ROW_TEXT
    override var createdAt: LocalDateTime = LocalDateTime.now()
}