package ie.wit.caloriepal.activities

import android.os.Bundle
import android.util.Log.i
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ie.wit.caloriepal.R
import ie.wit.caloriepal.helpers.calculateDaysUntilDeadline
import ie.wit.caloriepal.helpers.calculateDeficit
import ie.wit.caloriepal.main.MainApp
import ie.wit.caloriepal.models.UserModel
import kotlinx.android.synthetic.main.activity_user.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.time.LocalDate

class UserActivity: AppCompatActivity(), AnkoLogger {
    lateinit var app : MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info {"User goal Activity started"}
        setContentView(R.layout.activity_user)
        app = application as MainApp

        toolbarAddUser.title =title
        setSupportActionBar(toolbarAddUser)

        buttonAddUser.setOnClickListener{
            user.name = userNameField.text.toString()
            user.weight = startingWeightField.text.toString().toFloat()
            user.goal = goalWeightField.text.toString().toFloat()
            user.deadline= LocalDate.of(goalDatePicker.year, goalDatePicker.month +1 ,goalDatePicker.dayOfMonth)
            user.deficit = calculateDeficit(user)
        info{ "User details are: $user"}
            info{}
            info{"Todays date: ${LocalDate.now()}, Deadline: ${user.deadline}, days until deadline: ${calculateDaysUntilDeadline(user.deadline)}"}
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?) : Boolean {
        menuInflater.inflate(R.menu.add_in_progress_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.cancel_activity_button -> closeActivityOK()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun closeActivityOK(){
        setResult(RESULT_OK)
        finish()
    }

}