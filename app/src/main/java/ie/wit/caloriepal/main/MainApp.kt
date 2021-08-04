package ie.wit.caloriepal.main

import android.app.Application
import ie.wit.caloriepal.models.MealModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {
    var meals = mutableListOf<MealModel>()

    override fun onCreate() {
        super.onCreate()
        info("CaloriePal Started")
    }
}