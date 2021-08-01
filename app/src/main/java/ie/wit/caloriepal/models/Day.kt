package ie.wit.caloriepal.models

data class Day(
    var id : Long = 0,
    var meals: MutableList<Meal> = mutableListOf(),
    var defecit : Int = 0
)
