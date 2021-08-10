package ie.wit.caloriepal.activities

import android.os.Bundle
import android.view.Menu
import kotlinx.android.synthetic.main.activity_meal.*
import androidx.appcompat.app.AppCompatActivity
import ie.wit.caloriepal.R
import ie.wit.caloriepal.main.MainApp
import ie.wit.caloriepal.models.MealModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class MealActivity : AppCompatActivity(), AnkoLogger{

    lateinit var app : MainApp
    var meal = MealModel()
    var edit = false

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        info { "Meal Activity Started" }
        setContentView(R.layout.activity_meal)
        app = application as MainApp

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        buttonAddMeal.setOnClickListener {
            meal.title = mealNameField.text.toString()
            meal.caloricContent =  Integer.parseInt(caloricContentField.text.toString())
            if(mealNameField.text.isNotBlank()){
                app.mealStore.createOrUpdate(meal.copy(), edit)
                closeActivityOK()
            } else {
                toast("Please enter the meal details!")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_in_progress_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun closeActivityOK(){
        setResult(RESULT_OK)
        finish()
    }


}