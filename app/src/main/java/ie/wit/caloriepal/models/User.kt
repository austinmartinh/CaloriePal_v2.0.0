package ie.wit.caloriepal.models

import java.util.*

data class User(
    var id:Long = 0,
    var name: String = "",
    var weight : Float = 0f,
    var goal : Float = 0f,
    var deadline : Date,
    var defecit : Int = 0
)
