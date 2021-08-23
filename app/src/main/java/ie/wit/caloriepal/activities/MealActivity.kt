package ie.wit.caloriepal.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_meal.*
import androidx.appcompat.app.AppCompatActivity
import ie.wit.caloriepal.R
import ie.wit.caloriepal.helpers.readImage
import ie.wit.caloriepal.helpers.readImageFromPath
import ie.wit.caloriepal.helpers.showImagePicker
import ie.wit.caloriepal.main.MainApp
import ie.wit.caloriepal.models.Location
import ie.wit.caloriepal.models.MealModel
import org.jetbrains.anko.*
import java.time.LocalDate

class MealActivity : AppCompatActivity(), AnkoLogger {

    val IMAGE_REQ = 1
    val LOCATION_REQUEST = 2
    lateinit var app: MainApp
    var meal = MealModel()
    var edit = false
    var date: LocalDate = LocalDate.now()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info { "Meal Activity Started" }
        setContentView(R.layout.activity_meal)
        app = application as MainApp

        toolbarAdd.title = title
        R.id.cancel_activity_button
        setSupportActionBar(toolbarAdd)
        if (intent.hasExtra("date")) {
            var dateAsString = intent.extras?.getString("date")!!
            this.date = LocalDate.parse(dateAsString)
        }
        if (intent.hasExtra("meal_edit")) {
            edit = true
            meal = intent.extras?.getParcelable("meal_edit")!!
            mealNameField.setText(meal.title)
            caloricContentField.setText(meal.caloricContent.toString())
            notesField.setText(meal.notes)
            if(meal.image.isNotBlank()) {
                mealImageView.setImageBitmap(readImageFromPath(this, meal.image))
                mealImageView.adjustViewBounds = true
            }
            buttonAddMeal.text = getString(R.string.save_changes)
        }

        buttonAddMeal.setOnClickListener {
            meal.title = mealNameField.text.toString()
            meal.caloricContent = Integer.parseInt(caloricContentField.text.toString())
            meal.notes = notesField.text.toString()
            if (mealNameField.text.isNotBlank()) {
                app.mealStore.createOrUpdate(meal.copy(), date, edit)
                closeActivityOK()
            } else {
                toast("Please enter the meal details!")
            }
        }

        buttonAddImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQ)
        }

        buttonAddLocation.setOnClickListener {
            startActivityForResult(
                intentFor<MapActivity>().putExtra("location", meal.location.copy()),
                LOCATION_REQUEST
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_in_progress_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cancel_activity_button -> closeActivityOK()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQ -> {
                if (data != null) {
                    meal.image = data.data.toString()
                    mealImageView.setImageBitmap(readImage(this, resultCode, data))
                    buttonAddImage.text = getString(R.string.change_image)
                    mealImageView.adjustViewBounds = true
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras?.getParcelable<Location>("location")!!
                    meal.location = location
                }
            }
        }
    }

    private fun closeActivityOK() {
        setResult(RESULT_OK)
        finish()
    }
}