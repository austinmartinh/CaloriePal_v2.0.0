package ie.wit.caloriepal.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import ie.wit.caloriepal.R
import ie.wit.caloriepal.activities.MapActivity
import ie.wit.caloriepal.helpers.createImagePickerIntent
import ie.wit.caloriepal.helpers.readImage
import ie.wit.caloriepal.helpers.readImageFromPath
import ie.wit.caloriepal.main.MainApp
import ie.wit.caloriepal.models.Location
import ie.wit.caloriepal.models.MealModel
import kotlinx.android.synthetic.main.fragment_meal_add.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.io.IOException
import java.lang.Exception
import java.time.LocalDate

class MealAddFragment : Fragment(), AnkoLogger {

    val IMAGE_REQ = 1
    val LOCATION_REQUEST = 2
    lateinit var app: MainApp
    lateinit var root: View
    var meal = MealModel()
    var edit = false
    var date: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info { "Meal Activity Started" }

        app = activity?.application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        root = inflater.inflate(R.layout.fragment_meal_add, container, false)
        activity?.title = "Add Meal"

        processArguments()

        setButtonOnClickListeners()
        return root
    }

    override fun onResume() {
        super.onResume()
        processArguments()
    }

    private fun handleAddMealClicked() {
        meal.title = root.mealNameField.text.toString()
        if (root.caloricContentField.text.isNotBlank()) {
            meal.caloricContent = Integer.parseInt(root.caloricContentField.text.toString())
        }
        meal.notes = root.notesField.text.toString()
        if (root.mealNameField.text.isNotBlank()) {
            app.mealStore.createOrUpdate(meal.copy(), date, edit)
            triggerNavigate()
        } else {
            activity?.toast("Please enter the meal details!")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQ -> {
                if (data != null) {
                    meal.image = data.data.toString()
                    root.mealImageView.setImageBitmap(
                        readImage(
                            requireActivity(),
                            resultCode,
                            data
                        )
                    )
                    root.buttonAddImage.text = getString(R.string.change_image)
                    root.mealImageView.adjustViewBounds = true
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

    private fun processArguments() {
        if (arguments != null) {
            val extras = requireArguments().getBundle("extras")
            val dateAsString = extras?.getString("date")
            if (dateAsString != null) this.date = LocalDate.parse(dateAsString)

            val mealToEdit = extras?.getParcelable<MealModel>("meal_edit")
            if (mealToEdit != null) {
                this.meal = mealToEdit
                populateMealFields()
            }

        }
    }

    private fun populateMealFields() {
        edit = true
        root.mealNameField.setText(meal.title)
        root.caloricContentField.setText(meal.caloricContent.toString())
        root.notesField.setText(meal.notes)
        if (meal.image.isNotBlank()) {
            root.mealImageView.setImageBitmap(readImageFromPath(requireContext(), meal.image))
            root.mealImageView.adjustViewBounds = true
            root.buttonAddImage.text = getString(R.string.change_image)
        }
        root.buttonAddMeal.text = getString(R.string.save_changes)
    }

    private fun setButtonOnClickListeners() {
        root.buttonAddMeal.setOnClickListener {
            handleAddMealClicked()
        }
        root.buttonAddImage.setOnClickListener {
            startActivityForResult(createImagePickerIntent(), IMAGE_REQ)
        }
        root.buttonAddLocation.setOnClickListener {
            startActivityForResult(
                requireContext().intentFor<MapActivity>()
                    .putExtra("location", meal.location.copy()), LOCATION_REQUEST
            )
        }
    }

    private fun triggerNavigate() {
        val extras = Bundle()
        extras.putString("date", date.toString())
        extras.putBoolean("complete", true)
        setFragmentResult("listMealsRequest", bundleOf("extras" to extras))
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MealAddFragment().apply {
                arguments = Bundle().apply {}
            }
    }

}