package ie.wit.caloriepal.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.navigation.NavigationView
import ie.wit.caloriepal.R
import ie.wit.caloriepal.fragments.MealAddFragment
import ie.wit.caloriepal.fragments.MealListFragment
import ie.wit.caloriepal.fragments.UserFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import org.jetbrains.anko.toast

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AddUserListener {

    lateinit var ft : FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        navView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar, R.string.open_nav_drawer, R.string.close_nav_drawer
        )
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.secondary)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            if(bundle.getBoolean("bundleKey")) navigateTo(MealListFragment.newInstance())
        }


        ft = supportFragmentManager.beginTransaction()
        val fragment = MealAddFragment.newInstance()
        ft.replace(R.id.homeFrame, fragment)
        ft.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_mealList -> navigateTo(MealListFragment.newInstance())
            R.id.nav_user_details -> navigateTo(UserFragment.newInstance())
            R.id.nav_about -> navigateTo(MealAddFragment.newInstance())
            else -> toast("You Selected Something Else")
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.meal_list_menu, menu)
        return true
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        super.onBackPressed()
    }

    fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun triggerNavigateFromFragment(actionComplete: Boolean) {
        navigateTo(MealListFragment.newInstance())
    }


}

interface AddUserListener{
    fun triggerNavigateFromFragment(actionComplete: Boolean)
}