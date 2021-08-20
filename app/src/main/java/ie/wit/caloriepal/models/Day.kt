package ie.wit.caloriepal.models
/*
Data class for storing a days worth of meals.
Date is stored as string instead of LocalDate for ease of serialization as GSON is not
natively able to serialize LocalDate objects
 */
data class Day(
    var id : Long = 0,
    var meals: MutableList<MealModel> = mutableListOf(),
    var date : String = ""
)
