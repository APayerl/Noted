package se.payerl.noted.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.Serializable
import java.time.LocalDateTime

interface NoteBase : Serializable {
    val uuid: String
    val type: NoteType
    val createdAt: LocalDateTime
    var parent: String?
    var selected: MutableLiveData<Boolean>

    fun isDone(): Boolean
}