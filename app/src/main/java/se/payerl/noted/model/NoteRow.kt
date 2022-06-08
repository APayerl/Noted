package se.payerl.noted.model

interface NoteRow : NoteBase {
    var content: String
    val parent: String
}