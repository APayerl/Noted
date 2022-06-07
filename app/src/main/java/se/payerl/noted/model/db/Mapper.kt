package se.payerl.noted.model.db

import se.payerl.noted.model.Note
import se.payerl.noted.model.NoteRowAmount
import se.payerl.noted.model.NoteRowText

class Mapper {
    fun noteEntityToNote(noteEntity: NoteEntity): Note {
        val note = Note(noteEntity.uuid, noteEntity.type, noteEntity.createdAt)
        note.name = noteEntity.name
        return note
    }

    fun noteToNoteEntity(note: Note): NoteEntity {
        val entity = NoteEntity()
        with(note) {
            entity.name = name
            entity.uuid = uuid
            entity.createdAt = createdAt
        }
        return entity
    }

    fun noteRowTextToNoteRowTextEntity(row: NoteRowText): NoteRowTextEntity {
        val entity = NoteRowTextEntity()
        with(row) {
            entity.done = done
            entity.owner = owner
            entity.content = content
            entity.uuid = uuid
            entity.createdAt = createdAt
        }
        return entity
    }

    fun noteRowAmountToNoteRowAmountEntity(row: NoteRowAmount): NoteRowAmountEntity {
        val entity = NoteRowAmountEntity()
        with(row) {
            entity.amount = amount
            entity.amountDone = amountDone
            entity.content = content
            entity.uuid = uuid
            entity.createdAt = createdAt
        }
        return entity
    }
}