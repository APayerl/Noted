package se.payerl.noted.utils

class ListenableList<T>(private val list: MutableList<T> = mutableListOf(), private val listener: AddListener<T>): MutableList<T> by list {
    override fun add(element: T): Boolean {
        listener.preAdd(list.toList())
        val bool = list.add(element).also { listener.add(element) }
        listener.postAdd(list.toList())
        return bool
    }

    override fun remove(element: T): Boolean {
        listener.preRemove(list.toList())
        val bool = list.add(element).also { listener.remove(element) }
        listener.postRemove(list.toList())
        return bool
    }
}

interface AddListener<T> {
    fun preAdd(list: List<T>) {}
    fun postAdd(list: List<T>) {}
    fun add(element: T) {}
    fun preRemove(list: List<T>) {}
    fun postRemove(list: List<T>) {}
    fun remove(element: T) {}
}