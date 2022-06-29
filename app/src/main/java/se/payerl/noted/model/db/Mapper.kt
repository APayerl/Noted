package se.payerl.noted.model.db

import se.payerl.noted.model.*

class Mapper {
    fun noteEntityToNote(note: NoteEntity, db: AppDatabase): Note {
        return Note(note.uuid, note.type, note.createdAt, note.parent, note.name).apply {
            parent?.let { nonNullParent ->
                db.queryExecutor.execute {
                    db.mixedDao().getChildren(nonNullParent).map { child -> entityToBase(child, db) }.let { children ->
                        content.addAll(children)
                    }
                }
            }
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
            amountWhenFinished = row.amountWhenFinished.value!!
            parent = row.parent
            content = row.content
            uuid = row.uuid
            createdAt = row.createdAt
        }
    }

    fun noteRowTextEntityToNoteRowText(row: NoteRowTextEntity): NoteRowText {
        return NoteRowText(row.parent, row.content, row.done, row.uuid, row.createdAt, row.type)
    }

    fun noteRowAmountEntityToNoteRowAmount(row: NoteRowAmountEntity): NoteRowAmount {
        return NoteRowAmount(row.parent, row.content, row.amountWhenFinished, row.amount, row.uuid, row.type, row.createdAt)
    }

    fun entityToBase(entity: Entity, db: AppDatabase): NoteBase {
        return when(entity.type) {
            NoteType.LIST -> noteEntityToNote(entity as NoteEntity, db)
            NoteType.ROW_AMOUNT -> noteRowAmountEntityToNoteRowAmount(entity as NoteRowAmountEntity)
            NoteType.ROW_TEXT -> noteRowTextEntityToNoteRowText(entity as NoteRowTextEntity)
        }
    }
}