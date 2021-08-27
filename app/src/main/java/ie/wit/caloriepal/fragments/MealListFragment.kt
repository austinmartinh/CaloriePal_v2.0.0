package ie.wit.caloriepal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.caloriepal.R
import ie.wit.caloriepal.adapters.MealAdapter
import ie.wit.caloriepal.adapters.MealListener
import ie.wit.caloriepal.main.MainApp
import ie.wit.caloriepal.models.MealModel
import ie.wit.caloriepal.models.UserModel
import kotlinx.android.synthetic.main.fragment_meal_list.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.time.LocalDate

class MealListFragment() : Fragment(), AnkoLogger, MealListener {

    lateinit var app: MainApp
    lateinit var root: View

    var date: LocalDate = LocalDate.now()
    var meals = mutableListOf<MealModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        app = activity?.application as MainApp
        activity?.title = "Meals"
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_meal_list, container, false)

        root.buttonTomorrow.setOnClickListener {
            handleTomorrowClicked()
        }
        root.buttonYesterday.setOnClickListener {
            handleYesterdayClicked()
        }
        root.fabAdd.setOnClickListener{
            handleFabClick()
        }

        processArguments()
        updateDateText()
        root.mealRecyclerView.layoutManager = LinearLayoutManager(activity)
        loadMeals()

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MealListFragment().apply {}
    }

    override fun onMealClick(meal: MealModel) {
        val extras = Bundle()
        extras.putString("date", date.toString())
        extras.putParcelable("meal_edit", meal)

        triggerNavigate("addMealRequest", extras)
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
        if (day == null) day = app.mealStore.createDay(date)
        meals = day.meals

        root.mealRecyclerView.adapter = MealAdapter(
            meals, this
        ) { handleUpdateOrDelete() }
        root.mealRecyclerView.adapter?.notifyDataSetChanged()
    }

    //TODO Move check to home activity
    fun checkForNewUser() {
        if (app.userStore.user == UserModel()) {
//            startActivityForResult<UserActivity>(0)
        }
    }

    fun setCaloricAllowance() {
        val caloricAllowance = (2100 + app.userStore.user.deficit)
        root.dailyCalorieGoal.text = caloricAllowance.toString()
    }

    fun setTotalCalories(meals: List<MealModel>) {
        var totalCalories = 0
        for (meal in meals) {
            totalCalories += meal.caloricContent
        }
        root.dailyCalorieTotal.text = totalCalories.toString()
    }

    //Changes Caloric Values textColor to red if daily caloric limit exceeded, otherwise uses brand secondary color
    fun setCaloricTotalColours() {
        if (root.dailyCalorieTotal.text.toString().toInt() < root.dailyCalorieGoal.text.toString()
                .toInt()
        ) {
            root.dailyCalorieTotal.setTextColor(resources.getColor(R.color.secondary))
            root.dailyCalorieGoal.setTextColor(resources.getColor(R.color.secondary))
        } else {
            root.dailyCalorieTotal.setTextColor(resources.getColor(R.color.red))
            root.dailyCalorieGoal.setTextColor(resources.getColor(R.color.red))
        }
    }

    fun updateDateText() {
        val dateText = "${date.dayOfMonth} / ${date.month} / ${date.year}"
        root.dateValue?.text = dateText
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

    fun handleUpdateOrDelete() {
        setTotalCalories(meals)
        setCaloricTotalColours()
        app.mealStore.serialize()
    }

    fun handleFabClick(){
        val extras = Bundle()
        extras.putString("date", date.toString())
        triggerNavigate("addMealRequest", extras)
    }

    //TODO move to app toolbar
    fun handleClearUser() {
        app.mealStore.clearAllData()
        app.userStore.deleteUser()
        checkForNewUser()
    }

    private fun processArguments() {
        if (arguments != null) {
            val extras = requireArguments().getBundle("extras")
            val dateAsString = extras?.getString("date")
            if (dateAsString != null) date = LocalDate.parse(dateAsString)

        }
    }

    private fun triggerNavigate(requestKey: String, extras: Bundle) {
        setFragmentResult(requestKey, bundleOf("extras" to extras))
    }

}