package ie.wit.caloriepal.models

interface Store<T> {
    fun create(item : T)
    fun update(item : T)
    fun delete(item : T)
    fun findAll(): List<T>
    fun getById(id: Long) : T?
    fun serialize()
    fun deserialize()
}