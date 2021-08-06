package ie.wit.caloriepal.main

import android.app.Application
import ie.wit.caloriepal.models.MealJSONStore
import ie.wit.caloriepal.models.UserModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {
    lateinit var meals : MealJSONStore
    var user = UserModel()

    override fun onCreate() {
        super.onCreate()
        meals = MealJSONStore(applicationContext)
        info("CaloriePal Started")
    }
}