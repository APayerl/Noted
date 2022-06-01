package se.payerl.noted.model.db

interface Dao<T> {
    fun getAll(): List<T>
    fun findByContent(content: String): T
    fun findByUUID(uuid: String): T
    fun update(item: T)
    fun insertAll(vararg items: T)
    fun delete(item: T)
}