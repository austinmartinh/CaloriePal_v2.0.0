package ie.wit.caloriepal.models

interface Store {
    fun create(meal: MealModel)
    fun update(meal: MealModel)
    fun delete(meal: MealModel)
    fun findAll(): List<MealModel>
}