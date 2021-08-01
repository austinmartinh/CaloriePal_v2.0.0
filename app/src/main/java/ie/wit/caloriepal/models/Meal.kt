package ie.wit.caloriepal.models

import android.os.Parcelable
// The main model, meal. This will record the name and caloric content of a given meal as wel as the location at which it was eaten.
data class Meal(
    var id: Long =0,
    var title : String = "",
    var mealType : String = "",
    var caloricContent : Int = 0,
    var location: Location = Location()
)

data class Location(
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
)