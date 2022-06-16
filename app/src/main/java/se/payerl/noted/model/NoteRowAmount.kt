package se.payerl.noted.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDateTime
import java.util.*

class NoteRowAmount(
    override var parent: String?,
    var content: String = "",
    var _amountWhenFinished: Int = 1,
    var amount: Int = 0
) : NoteBase {
    override var uuid: String = UUID.randomUUID().toString()
    override var type: NoteType = NoteType.ROW_AMOUNT
    override var createdAt: LocalDateTime = LocalDateTime.now()
    val amountWhenFinished: MutableLiveData<Int>

    init {
        amountWhenFinished = MutableLiveData<Int>(_amountWhenFinished)
    }
}