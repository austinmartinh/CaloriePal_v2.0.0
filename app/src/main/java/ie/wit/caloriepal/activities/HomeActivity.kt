package ie.wit.caloriepal.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import ie.wit.caloriepal.R
import ie.wit.caloriepal.fragments.MealAddFragment
import ie.wit.caloriepal.fragments.MealListFragment
import ie.wit.caloriepal.fragments.UserFragment
import ie.wit.caloriepal.main.MainApp
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class HomeActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    ReturnToMealListListener,
    MealClickedInFragmentListener {

    lateinit var ft: FragmentTransaction
    lateinit var app: MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        toolbar.overflowIcon?.setTint(resources.getColor(R.color.secondary))

        app = application as MainApp
        navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer
        )
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.secondary)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportFragmentManager.setFragmentResultListener("listMealsRequest", this) { _, bundle ->
            val mealListFragment = MealListFragment.newInstance()
            mealListFragment.arguments = bundle
            navigateTo(mealListFragment)
        }
        supportFragmentManager.setFragmentResultListener("addMealRequest", this) { _, bundle ->
            val populatedAddFragment = MealAddFragment.newInstance()
            populatedAddFragment.arguments = bundle
            navigateTo(populatedAddFragment)
        }


        ft = supportFragmentManager.beginTransaction()
        val fragment = MealListFragment.newInstance()
        ft.replace(R.id.homeFrame, fragment)
        ft.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_mealList -> navigateTo(MealListFragment.newInstance())
            R.id.nav_user_details -> navigateTo(UserFragment.newInstance())
            R.id.nav_sign_out -> handleSignOut()
            else -> toast("You Selected Something Else")
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.meal_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear_all_data -> handleClearUser()
        }
        return super.onOptionsItemSelected(item)
    }

    fun handleClearUser() {
        app.mealStore.clearAllData()
        app.userStore.deleteUser()
        navigateTo(UserFragment.newInstance())
    }

    fun handleSignOut() {
        app.auth.signOut()
        startActivity<LoginActivity>()
        finish()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        super.onBackPressed()
    }

    fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, fragment)
            .addToBackStack(null)
            .commit()
    }


    override fun addUserFragmentCompleted(actionComplete: Boolean) {
        if (actionComplete) navigateTo(MealListFragment.newInstance())
    }

    override fun mealClickedOnInFragment() {
        navigateTo(MealListFragment.newInstance())
    }
}

interface ReturnToMealListListener {
    fun addUserFragmentCompleted(actionComplete: Boolean)
}

interface MealClickedInFragmentListener {
    fun mealClickedOnInFragment()
}