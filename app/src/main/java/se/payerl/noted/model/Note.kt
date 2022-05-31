package se.payerl.noted.model

class Note : NoteBase(NoteType.LIST) {
    var name: String = ""
    var content: MutableList<NoteBase> = mutableListOf()

    fun addNoteContent(newContent: NoteBase): Boolean {
        return this.content.add(newContent)
    }

    fun createAndAddNoteRow(): Boolean {
        return this.content.add(NoteRow(this.name))
    }
}

open class NoteRow(val owner: String, type: NoteType = NoteType.ROW, content: String = "", done: Boolean = false) : NoteBase(type) {
    var content: String
    var isDone: Boolean

    init {
        this.content = content
        this.isDone = done
    }
}

class NoteRowAmount(owner: String, type: NoteType = NoteType.ROW_AMOUNT) : NoteRow(owner, type) {
    var amount: Int = 1
}

enum class NoteType {
    LIST, ROW, ROW_AMOUNT
}

open class NoteBase(val type: NoteType)