package com.sigarda.jurnalkas.ui.activity


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sigarda.jurnalkas.R
import com.sigarda.jurnalkas.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.SmoothBottomBar

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        navController = findNavController(R.id.hostFragment)
        setupActionBarWithNavController(navController)
        setupSmoothBottomMenu()
        supportActionBar?.hide()
        setupNavigation()


navController.addOnDestinationChangedListener { _, destination, _ ->
    when (destination.id) {


        else -> hideBottomNav(false)
    }
}

}

private fun hideBottomNav(hide: Boolean) {
    if (hide) {
        binding.navView.visibility = View.GONE
    } else {
        binding.navView.visibility = View.VISIBLE
    }
}

override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.navigation_menu, menu)
    return true
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {

    }
    return super.onOptionsItemSelected(item)
}

//set an active fragment programmatically
fun setSelectedItem(pos:Int){
    binding.navView.setSelectedItem(pos)
}
//set badge indicator
fun setBadge(pos:Int){
    binding.navView.setBadge(pos)
}
//remove badge indicator
fun removeBadge(pos:Int){
    binding.navView.removeBadge(pos)
}

private fun showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

private fun setupSmoothBottomMenu() {
    val popupMenu = PopupMenu(this, null)
    popupMenu.inflate(R.menu.navigation_menu)
    val menu = popupMenu.menu
    //binding.bottomBar.setupWithNavController(menu, navController)
    binding.navView.setupWithNavController( navController)
}

override fun onSupportNavigateUp(): Boolean {
    return navController.navigateUp() || super.onSupportNavigateUp()
}

private fun setupNavigation() {
    // As we're inside a fragment calling `findNavController()` directly will crash the app
    // Hence, get a reference of `NavHostFragment`
    val navHostFragment =
        supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment


    // set navigation controller
    navController = navHostFragment.findNavController()

    // appbar configuration (for back button)
    val appBarConfiguration = AppBarConfiguration(navController.graph)
    setupActionBarWithNavController(navController, appBarConfiguration)
}

fun setBottomNavigationVisibility(visibility: Int) {
    binding.navView.visibility = visibility
}

fun setToolbarVisibility(isVisible: Boolean) {
    if (isVisible) supportActionBar?.show() else supportActionBar?.hide()
}


}