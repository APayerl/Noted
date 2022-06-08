package se.payerl.noted.model.db

interface Dao<T> {
    fun getAll(): List<T>
    fun findByContent(content: String): T?
    fun findByUUID(uuid: String): T?
    fun findByParent(uuid: String): List<T>
    fun update(item: T)
    fun insertAll(vararg items: T)
    fun insert(item: T)
    fun delete(item: T)
    fun hasUUID(uuid: String): Boolean
}