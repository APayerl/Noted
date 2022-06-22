package se.payerl.noted.model.db

import se.payerl.noted.model.NoteType
import java.time.LocalDateTime

interface Entity {
    var uuid: String
    var type: NoteType
    var createdAt: LocalDateTime
    var parent: String?
}