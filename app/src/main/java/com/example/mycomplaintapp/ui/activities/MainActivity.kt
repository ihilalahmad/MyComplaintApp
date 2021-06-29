package com.example.mycomplaintapp.ui.activities

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mycomplaintapp.R
import com.example.mycomplaintapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialInit()

    }

    private fun initialInit() {
        navView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.userLoginFragment,
                R.id.userRegistrationFragment
            )
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.userLoginFragment -> hideBottomNav()
                R.id.userRegistrationFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun showBottomNav() {
        binding.navView.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.navView.visibility = View.GONE
    }

    override fun onBackPressed() {
        navController.addOnDestinationChangedListener {_, destination, _ ->
            when(destination.id) {
                R.id.userLoginFragment -> finish()
            }
        }
        super.onBackPressed()
    }
}