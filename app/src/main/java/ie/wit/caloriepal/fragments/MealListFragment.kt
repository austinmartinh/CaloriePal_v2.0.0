package ie.wit.caloriepal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.caloriepal.R
import ie.wit.caloriepal.activities.MealActivity
import ie.wit.caloriepal.activities.UserActivity
import ie.wit.caloriepal.adapters.MealAdapter
import ie.wit.caloriepal.adapters.MealListener
import ie.wit.caloriepal.main.MainApp
import ie.wit.caloriepal.models.MealModel
import ie.wit.caloriepal.models.UserModel
import kotlinx.android.synthetic.main.activity_meal_list.*
import kotlinx.android.synthetic.main.fragment_meal_list.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import java.time.LocalDate

class MealListFragment() : Fragment(), AnkoLogger, MealListener {

    lateinit var app: MainApp

    var date: LocalDate = LocalDate.now()
    var meals= mutableListOf<MealModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        app = activity?.application as MainApp

        activity?.title = "Meals"
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_meal_list, container, false)

        root.buttonTomorrow.setOnClickListener{
            handleTomorrowClicked()
        }

        root.buttonYesterday.setOnClickListener{
           handleYesterdayClicked()
        }

        root.mealRecyclerView.setLayoutManager(LinearLayoutManager(activity))
        loadMeals()

        return root
    }

        companion object {
            @JvmStatic
            fun newInstance() =
                MealListFragment().apply {
                    arguments = Bundle().apply { }
                }
        }

    override fun onMealClick(meal: MealModel) {
        val extras = Bundle()
        extras.putString("date", date.toString())
        extras.putParcelable("meal_edit", meal)

//        startActivityForResult(
//            intentFor<MealActivity>().putExtras(extras),
//            0
//        )
    }

    fun loadMeals() {
        showMeals()
        setTotalCalories(meals)
        setCaloricAllowance()
        setCaloricTotalColours()
    }

    // Get day from list of days if date is in use, otherwise create and add day to list of days
    // Then, create a recycler view of the meals in that day
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
//            startActivityForResult<UserActivity>(0)
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

    //Changes Caloric Values textColor to red if daily caloric limit exceeded, otherwise uses brand secondary color
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

    private fun handleTomorrowClicked() {
        date = date.plusDays(1)
        updateDateText()
        loadMeals()
    }

    private fun handleYesterdayClicked() {
        date = date.minusDays(1)
        updateDateText()
        loadMeals()
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