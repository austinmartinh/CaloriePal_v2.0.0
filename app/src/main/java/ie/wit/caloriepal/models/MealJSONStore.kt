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

class MealJSONStore(val context: Context) : Store<MealModel> {

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
        val foundMeal = getById(meal.id)
        if (foundMeal != null) {
            foundMeal.title = meal.title
            foundMeal.caloricContent = meal.caloricContent
            foundMeal.notes = meal.notes
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
        val foundMeal= getById(meal.id)
        if (foundMeal != null) {
            meals.remove(foundMeal)
        }
        serialize()
    }

    override fun findAll(): MutableList<MealModel> {
        return meals
    }

    override fun getById(id:Long): MealModel? {
        return meals.find { p -> p.id == id }
    }

    override fun serialize(){
        val jsonString = gsonBuilder.toJson(meals, listType)
        write(context, JSON_MEAL_FILE, jsonString)
    }

    override fun deserialize() {
        val jsonString = read(context, JSON_MEAL_FILE)
        meals = Gson().fromJson(jsonString, listType)
    }
}

fun generateRandomId(): Long {
    return Random.nextLong()
}



