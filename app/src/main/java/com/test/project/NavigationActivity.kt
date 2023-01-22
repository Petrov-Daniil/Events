package com.test.project

import android.os.Bundle
import android.view.View
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.test.project.databinding.ActivityNavigationBinding
import com.test.project.ui.home.HomeFragment
import com.test.project.ui.home.full_news.FullNewsFragment
import com.test.project.ui.home_events.HomeEventFragment
import com.test.project.ui.home_events.add_event.AddEventFragment
import com.test.project.ui.home_events.full_event.FullEventFragment
import com.test.project.ui.login.LoginFragment
import com.test.project.ui.profile.ProfileFragment
import com.test.project.ui.schedule.ScheduleFragment


class NavigationActivity : AppCompatActivity() {

    private val binding: ActivityNavigationBinding by viewBinding()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(binding.root)
        onDestinationHideMenu()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.profileFragment,
                R.id.scheduleFragment,
                R.id.homeEventFragment
            )
        )
        binding.bottomNavigationMenu.setupWithNavController(navController)
        binding.bottomNavigationMenu.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    val bundle = Bundle()
                    bundle.putInt("position", 1)
                    navController.navigate(R.id.homeFragment, bundle)
                }
                R.id.homeEventFragment -> {
                    val bundle = Bundle()
                    bundle.putInt("position", 1)
                    navController.navigate(R.id.homeEventFragment, bundle)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun onDestinationHideMenu() {
        supportFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(
                fm: FragmentManager,
                f: Fragment,
                v: View,
                savedInstanceState: Bundle?
            ) {
                when (f) {
                    is FullNewsFragment -> {
                        hideBottomNavigationMenu()
                    }
                    is LoginFragment -> {
                        hideAppBar()
                        hideBottomNavigationMenu()
                    }
                    is FullEventFragment -> {
                        hideBottomNavigationMenu()
                    }
                    is AddEventFragment -> {
                        hideBottomNavigationMenu()
                    }
                    is HomeFragment,
                    is HomeEventFragment,
                    is ScheduleFragment,
                    is ProfileFragment -> {
                        showAppBar()
                        showBottomNavigationMenu()
                    }
                }
            }
        }, true)
    }

    private fun showBottomNavigationMenu() {
        binding.bottomNavigationMenu.visibility = View.VISIBLE
    }

    private fun hideBottomNavigationMenu() {
        binding.bottomNavigationMenu.visibility = View.GONE
    }

    private fun hideAppBar() {
        supportActionBar?.hide()
    }

    private fun showAppBar() {
        supportActionBar?.show()
    }
}




