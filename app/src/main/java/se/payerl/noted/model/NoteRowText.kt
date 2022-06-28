package se.payerl.noted.model

import androidx.lifecycle.MutableLiveData
import java.time.LocalDateTime
import java.util.*

class NoteRowText(
    override var parent: String?,
    var content: String = "",
    var done: Boolean = false,
    override var uuid: String = UUID.randomUUID().toString(),
    override var createdAt: LocalDateTime = LocalDateTime.now(),
    override var type: NoteType = NoteType.ROW_TEXT,
    override var selected: MutableLiveData<Boolean> = MutableLiveData(false)) : NoteBase {

    override fun isDone(): Boolean {
        return done
    }
}