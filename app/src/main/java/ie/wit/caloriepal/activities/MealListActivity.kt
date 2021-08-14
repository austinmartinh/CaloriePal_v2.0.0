package ie.wit.caloriepal.activities

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.caloriepal.R
import ie.wit.caloriepal.adapters.MealAdapter
import ie.wit.caloriepal.adapters.MealListener
import ie.wit.caloriepal.main.MainApp
import ie.wit.caloriepal.models.MealModel
import ie.wit.caloriepal.models.UserModel
import kotlinx.android.synthetic.main.activity_meal_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult

class MealListActivity() : AppCompatActivity(), MealListener, AnkoLogger {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_list)
        app = application as MainApp

        checkForNewUser()
        info { "User Details after new user check: ${app.userStore.user}" }

        mealListToolbar.title = title
        setSupportActionBar(mealListToolbar)

        val layoutManager = LinearLayoutManager(this)
        mealRecyclerView.layoutManager = layoutManager
        loadMeals()
    }

    override fun onResume() {
        super.onResume()
        loadMeals()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.meal_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.meal_add -> startActivityForResult<MealActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMealClick(meal: MealModel) {
        startActivityForResult(
            intentFor<MealActivity>().putExtra("meal_edit", meal),
            0
        )
    }

    fun loadMeals() {
        showMeals()
        setTotalCalories(app.mealStore.findAll())
        setCaloricAllowance()
        setCaloricTotalColours()
    }

    fun showMeals() {
        mealRecyclerView.adapter = MealAdapter(app.mealStore.findAll(), this
        ) { handleUpdateOrDelete() }
        mealRecyclerView.adapter?.notifyDataSetChanged()
    }

    fun checkForNewUser() {
        //if(app.userStore.users.size == 0 ){
        if (app.userStore.user == UserModel()) {
            startActivityForResult<UserActivity>(0)
        } else {
            //TODO present user with list of users to choose from
        }
    }

    fun setCaloricAllowance() {
        val caloricAllowance = (2100 + app.userStore.user.deficit)
        dailyCalorieGoal.text = caloricAllowance.toString()
    }

    fun setTotalCalories(meals: List<MealModel>) {
        var totalCalories = 0
        for (meal in meals) {
            totalCalories += meal.caloricContent
        }
        dailyCalorieTotal.text = totalCalories.toString()
    }

    fun setCaloricTotalColours() {
        if (dailyCalorieTotal.text.toString().toInt() < dailyCalorieGoal.text.toString().toInt()) {
            dailyCalorieTotal.setTextColor(Color.GREEN)
            dailyCalorieGoal.setTextColor(Color.GREEN)
        } else {
            dailyCalorieTotal.setTextColor(Color.RED)
            dailyCalorieGoal.setTextColor(Color.RED)
        }
    }

    fun handleUpdateOrDelete(){
        setTotalCalories(app.mealStore.findAll())
        setCaloricTotalColours()
        app.mealStore.serialize()
    }
}