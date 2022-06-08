package se.payerl.noted

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import se.payerl.noted.fragments.AboutFragment
import se.payerl.noted.fragments.AboutFragmentDirections
import se.payerl.noted.fragments.OverviewFragment
import se.payerl.noted.fragments.OverviewFragmentDirections

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var abdt: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = drawer.findViewById<NavigationView>(R.id.nav_view)

        setSupportActionBar(toolbar)
        toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_menu_black_24, null)
        abdt = ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        abdt.isDrawerIndicatorEnabled = true
        drawer.addDrawerListener(abdt)

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.drawer_list_overview -> findNavController(R.id.nav_host_fragment).navigate(NavGraphDirections.globalToAboutFragment())
                R.id.drawer_about -> findNavController(R.id.nav_host_fragment).navigate(NavGraphDirections.globalToOverviewFragment())
            }
            drawer.close()
            true
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        abdt.syncState()
    }
}