package ie.wit.caloriepal.models

import java.time.LocalDate
import java.util.*

data class UserModel(
    var id:Long = 0,
    var name: String = "",
    var weight : Float = 0f,
    var goal : Float = 0f,
    var deadline: LocalDate = LocalDate.MIN,
    var deficit : Int = 0
)
