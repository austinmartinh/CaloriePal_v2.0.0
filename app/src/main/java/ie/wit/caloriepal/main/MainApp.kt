package ie.wit.caloriepal.main

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import ie.wit.caloriepal.models.MealJSONStore
import ie.wit.caloriepal.models.UserJSONStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {
    lateinit var mealStore : MealJSONStore
    lateinit var userStore : UserJSONStore
    lateinit var auth: FirebaseAuth


    override fun onCreate() {
        super.onCreate()
        mealStore = MealJSONStore(applicationContext)
        userStore = UserJSONStore(applicationContext)
        info{"User details: ${userStore.user}"}
        info("CaloriePal Started")
    }
}