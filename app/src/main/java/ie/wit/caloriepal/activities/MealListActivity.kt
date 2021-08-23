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
import java.time.LocalDate

class MealListActivity() : AppCompatActivity(), MealListener, AnkoLogger {

    lateinit var app: MainApp

    var date:LocalDate = LocalDate.now()
    var meals= mutableListOf<MealModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_list)
        app = application as MainApp

        checkForNewUser()
        mealListToolbar.title = title
        setSupportActionBar(mealListToolbar)

        updateDateText()

        buttonTomorrow.setOnClickListener{
            info{"BUTTON TOMORROW CLICKED"}
            date = date.plusDays(1)
            updateDateText()
            loadMeals()
        }

        buttonYesterday.setOnClickListener{
            info{"BUTTON TOMORROW CLICKED"}
            date = date.minusDays(1)
            updateDateText()
            loadMeals()
        }

        val layoutManager = LinearLayoutManager(this)
        mealRecyclerView.layoutManager = layoutManager
        loadMeals()
    }

    override fun onResume() {
        super.onResume()
        loadMeals()
        setMenuUserIcon()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.meal_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.meal_add -> startActivityForResult(
                intentFor<MealActivity>().putExtra("date", date.toString()),
        0)
            R.id.user_details -> handleClearUser()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMealClick(meal: MealModel) {
        val extras = Bundle()
        extras.putString("date", date.toString())
        extras.putParcelable("meal_edit", meal)

        startActivityForResult(
            intentFor<MealActivity>().putExtras(extras),
            0
        )
    }

    fun loadMeals() {
        showMeals()
        setTotalCalories(meals)
        setCaloricAllowance()
        setCaloricTotalColours()
    }

    // Get day from list of days if date is in use, otherwise create and add day to list of days
    // Then, create a recycler view of the meals in that day, which may be no meals
    fun showMeals() {
        var day = app.mealStore.getDayByDate(date)
        if(day==null) day = app.mealStore.createDay(date)
        meals = day.meals

        mealRecyclerView.adapter = MealAdapter(meals, this
        ) { handleUpdateOrDelete() }
        mealRecyclerView.adapter?.notifyDataSetChanged()
    }

    fun checkForNewUser() {
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
            dailyCalorieTotal.setTextColor(resources.getColor(R.color.secondary))
            dailyCalorieGoal.setTextColor(resources.getColor(R.color.secondary))
        } else {
            dailyCalorieTotal.setTextColor(resources.getColor(R.color.red))
            dailyCalorieGoal.setTextColor(resources.getColor(R.color.red))
        }
    }

    fun updateDateText(){
        val dateText = "${date.dayOfMonth} / ${date.month} / ${date.year}"
        dateValue.text = dateText
    }

    fun handleUpdateOrDelete(){
        setTotalCalories(meals)
        setCaloricTotalColours()
        app.mealStore.serialize()
    }

    fun handleClearUser(){
        app.mealStore.clearAllData()
        app.userStore.deleteUser()
        checkForNewUser()
    }

    fun setMenuUserIcon(){
        if(app.userStore.user != UserModel()){
            info{"USER EQUAL TO DEFAULT VALUES!"}
            mealListToolbar.menu.findItem(R.id.user_details)?.setIcon(R.drawable.ic_baseline_person_off_36)
        }else{
            mealListToolbar.menu.findItem(R.id.user_details)?.setIcon(R.drawable.ic_baseline_person_36)
        }
    }

}