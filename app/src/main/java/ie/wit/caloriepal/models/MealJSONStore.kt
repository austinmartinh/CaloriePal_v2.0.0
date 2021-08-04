package ie.wit.caloriepal.models

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.*
import java.lang.Exception
import kotlin.random.Random

val JSON_MEAL_FILE = "meals.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<ArrayList<MealModel>>() {}.type

class MealJSONStore(val context: Context) : MealStore {

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


interface MealStore {
    fun create(meal: MealModel)
    fun update(meal: MealModel)
    fun delete(meal: MealModel)
    fun findAll(): List<MealModel>
}

fun generateRandomId(): Long {
    return Random.nextLong()
}

fun read(context:Context, fileName:String) : String {
    var str =""
    try{
        val inputStream = context.openFileInput(fileName)
        if(inputStream != null){
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val partialStr = StringBuilder()
            var done = false
            while(!done){
                val line = bufferedReader.readLine()
                done = (line == null )
                if(line!=null) partialStr.append(line)
            }
            inputStream.close()
            str =partialStr.toString()
        }
    } catch (e:FileNotFoundException){
        Log.e("Error: ", "file not found $e")
    } catch (e:IOException){
        Log.e("Error: ", "could not read file $e")
    }
    return str
}

fun write(context: Context, fileName: String, data:String){
    try {
        val outPutStreamWriter =
            OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE))
        outPutStreamWriter.write(data)
        outPutStreamWriter.close()
    } catch (e: Exception) {
        Log.e("Error: ", "Cannot read file: $e")
    }
}

fun exists(context: Context, fileName: String) : Boolean {
    val file = context.getFileStreamPath(fileName)
    return file.exists()
}

