package se.payerl.noted.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.time.LocalDateTime
import java.util.*

class NoteRowAmount(
    override var parent: String?,
    var content: String = "",
    private val _amountWhenFinished: Int = 1,
    var amount: Int = 0,
    override var uuid: String = UUID.randomUUID().toString(),
    override var type: NoteType = NoteType.ROW_AMOUNT,
    override var createdAt: LocalDateTime = LocalDateTime.now(),
    override var selected: MutableLiveData<Boolean> = MutableLiveData(false)
) : NoteBase {
    override fun isDone(): Boolean {
        return amountWhenFinished.value == amount
    }

    val amountWhenFinished: MutableLiveData<Int> = MutableLiveData<Int>(_amountWhenFinished)
}