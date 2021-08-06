package ie.wit.caloriepal.helpers

import ie.wit.caloriepal.models.UserModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

const val CALORIES_PER_KG = 7000

fun calculateDeficit(user: UserModel) : Int {
    val totalDeficit = (user.startWeight - user.goalWeight).toInt()  * CALORIES_PER_KG
    return totalDeficit / calculateDaysUntilDeadline(user.deadline)
}

fun calculateDaysUntilDeadline(deadline:LocalDate) : Int {
        return (ChronoUnit.DAYS.between(LocalDate.now(), deadline)).toInt()
}

