package ie.wit.caloriepal.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ie.wit.caloriepal.R
import ie.wit.caloriepal.helpers.calculateDaysUntilDeadline
import ie.wit.caloriepal.helpers.calculateDeficit
import ie.wit.caloriepal.main.MainApp
import ie.wit.caloriepal.models.UserModel
import kotlinx.android.synthetic.main.activity_user.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.time.LocalDate

class UserActivity : AppCompatActivity(), AnkoLogger {
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info { "User goal Activity started" }
        setContentView(R.layout.activity_user)
        app = application as MainApp

        toolbarAddUser.title = title
        setSupportActionBar(toolbarAddUser)

        Toast.makeText(this, "New User Detected, please enter your details", Toast.LENGTH_LONG).show()

        buttonAddUser.setOnClickListener {
            validateUserDetails()
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

    private fun closeActivityOK() {
        setResult(RESULT_OK)
        finish()
    }

    private fun validateUserDetails() {
        var startingWeight = startingWeightField.text.toString().toFloatOrNull()
        var goalWeight = goalWeightField.text.toString().toFloatOrNull()
        var deadline = LocalDate.of(goalDatePicker.year,goalDatePicker.month + 1, goalDatePicker.dayOfMonth)

        if (startingWeight ==null) startingWeight = 0f
        if(goalWeight== null) goalWeight = 0f

        if (startingWeight <= 0 || goalWeight <= 0) {
            Toast.makeText(this, "Starting and goal weight must be above zero", Toast.LENGTH_SHORT).show()
            return
        }
        if (startingWeight <= goalWeight) {
            Toast.makeText(this, "Your goal weight should be below your current weight", Toast.LENGTH_SHORT).show()
            return
        }
        if (calculateDaysUntilDeadline(deadline) <= 0) {
            Toast.makeText(this, "Your goal date should be in the future", Toast.LENGTH_SHORT).show()
            return
        }
        app.user.name = userNameField.text.toString()
        app.user.startWeight = startingWeight
        app.user.goalWeight = goalWeight
        app.user.deadline = deadline
        app.user.deficit = calculateDeficit(app.user)
        info { "User details are: $app.user" }
        closeActivityOK()
    }
}
