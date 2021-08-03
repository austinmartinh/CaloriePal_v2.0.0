package ie.wit.caloriepal.activities

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_meal.*
import androidx.appcompat.app.AppCompatActivity
import ie.wit.caloriepal.R
import ie.wit.caloriepal.models.MealModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class MealActivity : AppCompatActivity(), AnkoLogger{

    var meal = MealModel()
    var meals = mutableListOf<MealModel>()

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        info { "Meal Activity Started" }
        setContentView(R.layout.activity_meal)


        buttonAddMeal.setOnClickListener {
            meal.title = mealNameField.text.toString()
            meal.caloricContent =  Integer.parseInt(caloricContentField.text.toString())
            if(mealNameField.text.isNotBlank()){
                meals.add(meal.copy())
                closeActivityOK()
            } else {
                toast("Please enter the meal details!")
            }
        }
    }

    private fun closeActivityOK(){
        setResult(RESULT_OK)
        finish()
    }


}