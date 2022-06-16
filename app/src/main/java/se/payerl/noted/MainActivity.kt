package se.payerl.noted

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var abdt: ActionBarDrawerToggle
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById<Toolbar>(R.id.toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navView = drawer.findViewById<NavigationView>(R.id.nav_view)

        setSupportActionBar(toolbar)
        toolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_menu_black_24, null)
        abdt = ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        abdt.isDrawerIndicatorEnabled = true
        drawer.addDrawerListener(abdt)

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.drawer_list_root -> findNavController(R.id.nav_host_fragment).navigate(NavGraphDirections.globalToListViewFragment())
                R.id.drawer_about -> findNavController(R.id.nav_host_fragment).navigate(NavGraphDirections.globalToAboutFragment())
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