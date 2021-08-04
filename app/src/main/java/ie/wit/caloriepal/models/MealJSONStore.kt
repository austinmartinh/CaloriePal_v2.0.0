package ie.wit.caloriepal.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ie.wit.caloriepal.helpers.*
import kotlin.random.Random

val JSON_MEAL_FILE = "meals.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<ArrayList<MealModel>>() {}.type

class MealJSONStore(val context: Context) : Store {

    var meals = mutableListOf<MealModel>()

    init{
        if(exists(context, JSON_MEAL_FILE)){
            deserialize()
        }
    }

    override fun create(meal: MealModel) {
        meal.id = generateRandomId()
        meals.add(meal)
    }

    override fun update(meal: MealModel) {
        val foundMeal: MealModel? = meals.find { p -> p.id == meal.id }
        if (foundMeal != null) {
            foundMeal.title = meal.title
            foundMeal.caloricContent = meal.caloricContent
        }
    }

    fun createOrUpdate(meal: MealModel, edit: Boolean){
        if(edit){
            update(meal)
        }else{
            create(meal)
        }
        serialize()
    }

    override fun delete(meal: MealModel) {
        val foundMeal: MealModel? = meals.find { p -> p.id == meal.id }
        if (foundMeal != null) {
            meals.remove(foundMeal)
        }
        serialize()
    }

    override fun findAll(): List<MealModel> {
        return meals
    }
    fun serialize(){
        val jsonString = gsonBuilder.toJson(meals, listType)
        write(context, JSON_MEAL_FILE, jsonString)
    }

    fun deserialize() {
        val jsonString = read(context, JSON_MEAL_FILE)
        meals = Gson().fromJson(jsonString, listType)
    }
}

fun generateRandomId(): Long {
    return Random.nextLong()
}



