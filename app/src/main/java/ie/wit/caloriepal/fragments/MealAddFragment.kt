package ie.wit.caloriepal.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ie.wit.caloriepal.R
import ie.wit.caloriepal.main.MainApp
import ie.wit.caloriepal.models.Location
import ie.wit.caloriepal.models.MealModel
import kotlinx.android.synthetic.main.fragment_meal_add.*
import kotlinx.android.synthetic.main.fragment_meal_add.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.time.LocalDate

class MealAddFragment : Fragment(), AnkoLogger {

    val IMAGE_REQ = 1
    val LOCATION_REQUEST = 2
    lateinit var app: MainApp
    var meal = MealModel()
    var edit = false
    var date: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info { "Meal Activity Started" }

        app = activity?.application as MainApp

//        if (intent.hasExtra("date")) {
//            getDateIfPresent()
//        }
//        if (intent.hasExtra("meal_edit")) {
//            populateMealFields()
//        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {

        val root = inflater.inflate(R.layout.fragment_meal_add, container, false)
        activity?.title = "Add Meal"

        root.buttonAddMeal.setOnClickListener {
            handleAddMealClicked(root)
        }
//        buttonAddImage.setOnClickListener {
//            showImagePicker(this, IMAGE_REQ)
//        }
//        buttonAddLocation.setOnClickListener {
//            startActivityForResult(
//                intentFor<MapActivity>().putExtra("location", meal.location.copy()),
//                LOCATION_REQUEST
//            )
//        }
        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MealAddFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    private fun getDateIfPresent() {
//        var dateAsString = intent.extras?.getString("date")!!
//        this.date = LocalDate.parse(dateAsString)
    }

    private fun populateMealFields() {
        edit = true
//        meal = intent.extras?.getParcelable("meal_edit")!!
        view?.mealNameField?.setText(meal.title)
        view?.caloricContentField?.setText(meal.caloricContent.toString())
        notesField.setText(meal.notes)
//        if (meal.image.isNotBlank()) {
//            mealImageView.setImageBitmap(readImageFromPath(this, meal.image))
//            mealImageView.adjustViewBounds = true
//        }
        buttonAddMeal.text = getString(R.string.save_changes)
    }

    private fun handleAddMealClicked(layout:View) {
        meal.title = layout.mealNameField.text.toString()
        if(layout.caloricContentField.text.isNotBlank()) {
            meal.caloricContent = Integer.parseInt(layout.caloricContentField.text.toString())
        }
        meal.notes = layout.notesField.text.toString()
        if (layout.mealNameField.text.isNotBlank()) {
            app.mealStore.createOrUpdate(meal.copy(), date, edit)
//            closeActivityOK()
        } else {
            activity?.toast("Please enter the meal details!")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
//            R.id.cancel_activity_button -> closeActivityOK()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQ -> {
                if (data != null) {
                    meal.image = data.data.toString()
//                    mealImageView.setImageBitmap(readImage(this, resultCode, data))
//                    buttonAddImage.text = getString(R.string.change_image)
//                    mealImageView.adjustViewBounds = true
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
}