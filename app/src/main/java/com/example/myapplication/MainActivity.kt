package com.example.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var loaderContainer: View
    private lateinit var loaderView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        loaderContainer = findViewById(R.id.fullscreen_loader)
        loaderView = findViewById(R.id.view)

        loaderContainer.visibility = View.GONE

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    cerrarSesion()
                    drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

    private fun cerrarSesion() {
        showLoader()

        Handler(Looper.getMainLooper()).postDelayed({
            val navController = findNavController(R.id.nav_host_fragment)

            navController.navigate(R.id.action_taskListFragment_to_loginFragment)

            hideLoader()
        }, 1500)
    }


    fun iniciarSesion() {
        showLoader()

        Handler(Looper.getMainLooper()).postDelayed({
            val navController = findNavController(R.id.nav_host_fragment)

            navController.navigate(R.id.action_loginFragment_to_taskListFragment)

            hideLoader()
        }, 1500)
    }


    fun showLoader() {
        loaderContainer.visibility = View.VISIBLE
        loaderView.playAnimation()
    }

    fun hideLoader() {
        loaderView.pauseAnimation()
        loaderContainer.visibility = View.GONE
    }

    fun toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}
