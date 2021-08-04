package ie.wit.caloriepal.main

import android.app.Application
import ie.wit.caloriepal.models.MealJSONStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {
    lateinit var meals : MealJSONStore

    override fun onCreate() {
        super.onCreate()
        meals = MealJSONStore(applicationContext)
        info("CaloriePal Started")
    }
}