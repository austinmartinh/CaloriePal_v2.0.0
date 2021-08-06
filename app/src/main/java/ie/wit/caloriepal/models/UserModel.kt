package ie.wit.caloriepal.models

import java.time.LocalDate

data class UserModel(
    var id:Long = 0,
    var name: String = "User",
    var startWeight : Float = 0f,
    var goalWeight : Float = 0f,
    var deadline: LocalDate = LocalDate.MIN,
    var deficit : Int = 0
)
