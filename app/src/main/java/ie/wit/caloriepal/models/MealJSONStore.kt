package ie.wit.caloriepal.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import ie.wit.caloriepal.helpers.*
import org.jetbrains.anko.AnkoLogger
import java.time.LocalDate
import kotlin.random.Random

val JSON_MEAL_FILE = "meals.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<ArrayList<Day>>() {}.type

/*
Class for CRUD of meal objects. Stores list of Days which contain relevant dates and lists of meals
Also writes and reads to and from Json for persistent storage.
 */
class MealJSONStore(val context: Context) : Store<MealModel>, AnkoLogger {

    var allMeals = mutableListOf<Day>()

    init {
        if (exists(context, JSON_MEAL_FILE)) {
            deserialize()
        }
    }

    override fun create(meal:MealModel, date:LocalDate) {
        var day = getDayByDate(date)
        if(day == null){
            day = createDay(date)
        }
        meal.id = generateRandomId()
        day.meals.add(meal)
    }

    fun createDay(date: LocalDate):Day{
        val newDay = Day(date = date.toString(), id = generateRandomId())
        allMeals.add(newDay)
        return newDay
    }

    override fun update(meal:MealModel, date:LocalDate){
        var day = getDayByDate(date)
        if(day !=null) {
            val foundMeal = getById(meal.id, day.meals)
            if (foundMeal != null) {
                foundMeal.title = meal.title
                foundMeal.caloricContent = meal.caloricContent
                foundMeal.notes = meal.notes
                foundMeal.image = meal.image
                foundMeal.location = meal.location
            }
        }
    }

    fun createOrUpdate(meal: MealModel,date: LocalDate, edit: Boolean) {
        if (edit) {
            update(meal, date)
        } else {
            create(meal, date)
        }
        serialize()
    }

     override fun delete(meal:MealModel, date:LocalDate) {
         var day = getDayByDate(date)
         if(day !=null) {
             val foundMeal = getById(meal.id, day.meals)
             if (foundMeal != null) {
                 day.meals.remove(foundMeal)
             }
             serialize()
         }
     }

    fun findAll(): MutableList<Day> {
        return allMeals
    }

     fun getById(id: Long, meals:MutableList<MealModel>): MealModel? {
        return meals.find { p -> p.id == id }
    }

    fun getDayByDate(date:LocalDate) : Day? {
        return allMeals.find { p -> p.date== date.toString()}
    }

    override fun serialize() {
        val jsonString = gsonBuilder.toJson(allMeals, listType)
        write(context, JSON_MEAL_FILE, jsonString)
    }

    override fun deserialize() {
        val jsonString = read(context, JSON_MEAL_FILE)
        allMeals = Gson().fromJson(jsonString, listType)
    }
}

fun generateRandomId(): Long {
    return Random.nextLong()
}



