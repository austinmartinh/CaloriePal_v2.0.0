package ie.wit.caloriepal.models

import java.time.LocalDate

interface Store<T> {
    fun create(item : T, date:LocalDate)
    fun update(item : T, date:LocalDate)
    fun delete(item : T, date:LocalDate)
    fun serialize()
    fun deserialize()
}