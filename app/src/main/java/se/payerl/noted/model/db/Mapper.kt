package se.payerl.noted.model.db

import se.payerl.noted.model.Note
import se.payerl.noted.model.NoteRowAmount
import se.payerl.noted.model.NoteRowText
import se.payerl.noted.model.NoteType
import java.time.LocalDateTime
import java.util.*

class Mapper {
    fun noteEntityToNote(noteEntity: NoteEntity): Note {
        return Note(noteEntity.uuid, noteEntity.type, noteEntity.createdAt, noteEntity.parent).apply {
            name = noteEntity.name
        }
    }

    fun noteToNoteEntity(note: Note): NoteEntity {
        return NoteEntity().apply {
            name = note.name
            uuid = note.uuid
            createdAt = note.createdAt
            parent = note.parent
        }
    }

    fun noteRowTextToNoteRowTextEntity(row: NoteRowText): NoteRowTextEntity {
        return NoteRowTextEntity().apply {
            done = row.done
            parent = row.parent
            content = row.content
            uuid = row.uuid
            createdAt = row.createdAt
        }
    }

    fun noteRowAmountToNoteRowAmountEntity(row: NoteRowAmount): NoteRowAmountEntity {
        return NoteRowAmountEntity().apply {
            amount = row.amount
            amountDone = row.amountDone
            parent = row.parent
            content = row.content
            uuid = row.uuid
            createdAt = row.createdAt
        }
    }

    fun noteRowTextEntityToNoteRowText(row: NoteRowTextEntity): NoteRowText {
        return NoteRowText(row.parent).apply {
            content = row.content
            done = row.done
            uuid = row.uuid
            type = row.type
            createdAt = row.createdAt
        }
    }

    fun noteRowAmountEntityToNoteRowAmount(row: NoteRowAmountEntity): NoteRowAmount {
        return NoteRowAmount(row.parent).apply {
            content = row.content
            amountDone = row.amountDone
            amount = row.amount
            uuid = row.uuid
            type = row.type
            createdAt = row.createdAt
        }
    }
}