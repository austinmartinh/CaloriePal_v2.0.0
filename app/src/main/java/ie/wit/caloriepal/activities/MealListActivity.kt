package ie.wit.caloriepal.activities

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
import org.jetbrains.anko.startActivityForResult

class MealListActivity() : AppCompatActivity(), MealListener {

    lateinit var app:MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_list)
        app = application as MainApp

        checkForNewUser(app.user)

        mealListToolbar.title = title
        setSupportActionBar(mealListToolbar)

        val layoutManager = LinearLayoutManager(this)
        mealRecyclerView.layoutManager = layoutManager
        loadMeals()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.meal_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.meal_add -> startActivityForResult<MealActivity>(0)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMealClick(meal: MealModel) {
       //TODO
    }

    fun loadMeals(){
        showMeals(app.meals.findAll())
    }

    fun showMeals(meals:List<MealModel>) {
        mealRecyclerView.adapter = MealAdapter(meals, this)
        mealRecyclerView.adapter?.notifyDataSetChanged()
    }

    fun checkForNewUser(user:UserModel){
        //loadUser()
        promptNewUser(user)
    }

    fun promptNewUser(user: UserModel){
        if(user == UserModel()){
            startActivityForResult<UserActivity>(0)
        }
    }

}