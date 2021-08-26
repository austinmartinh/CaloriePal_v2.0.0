package ie.wit.caloriepal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import ie.wit.caloriepal.R
import ie.wit.caloriepal.helpers.calculateDaysUntilDeadline
import ie.wit.caloriepal.helpers.calculateDeficit
import ie.wit.caloriepal.main.MainApp
import ie.wit.caloriepal.models.UserModel
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.fragment_user.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.time.LocalDate
import androidx.fragment.app.setFragmentResult

class UserFragment() : Fragment(), AnkoLogger {
    lateinit var app: MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_user, container, false)

        root.buttonAddUser.setOnClickListener {
            createValidUser(root)
        }
        return root
    }

    private fun createValidUser(root:View){
        val user = validateUserDetails()
        if(user!=null){
            app.userStore.createOrUpdate(user.copy(), false)
            triggerNavigate()
        }
    }

    private fun validateUserDetails():UserModel? {
        var startingWeight = startingWeightField.text.toString().toFloatOrNull()
        var goalWeight = goalWeightField.text.toString().toFloatOrNull()
        var deadline = LocalDate.of(goalDatePicker.year,goalDatePicker.month + 1, goalDatePicker.dayOfMonth)

        if (startingWeight ==null) startingWeight = 0f
        if(goalWeight== null) goalWeight = 0f

        if (startingWeight <= 0 || goalWeight <= 0) {
            Toast.makeText(this.context, "Starting and goal weight must be above zero", Toast.LENGTH_SHORT).show()
            return null
        }
        if (startingWeight <= goalWeight) {
            Toast.makeText(this.context, "Your goal weight should be below your current weight", Toast.LENGTH_SHORT).show()
            return null
        }
        if (calculateDaysUntilDeadline(deadline) <= 0) {
            Toast.makeText(this.context, "Your goal date should be in the future", Toast.LENGTH_SHORT).show()
            return null
        }

        user.name = userNameField.text.toString()
        user.startWeight = startingWeight
        user.goalWeight = goalWeight
        user.deadline = deadline
        user.deficit = calculateDeficit(user)
        info { "User details are: $user" }
        return user.copy()
    }
    private fun triggerNavigate(){
        setFragmentResult("requestKey", bundleOf("bundleKey" to true))
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserFragment().apply {}
    }
}