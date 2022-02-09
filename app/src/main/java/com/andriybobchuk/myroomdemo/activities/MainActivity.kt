package com.andriybobchuk.myroomdemo.activities


import android.app.PendingIntent.getActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.andriybobchuk.myroomdemo.databinding.ActivityMainBinding
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.FragmentTransaction
import com.andriybobchuk.myroomdemo.R
import com.andriybobchuk.myroomdemo.databinding.ContentMainBinding
import com.andriybobchuk.myroomdemo.databinding.DrawerHeaderBinding
import com.andriybobchuk.myroomdemo.room.AccountDao
import com.andriybobchuk.myroomdemo.room.CategoryDao
import com.andriybobchuk.myroomdemo.util.FinanceApp
import com.andriybobhcuk.manageux.activities.BaseActivity
import com.google.android.material.shape.CornerFamily

import com.google.android.material.shape.MaterialShapeDrawable


import com.google.android.material.navigation.NavigationView


class MainActivity : BaseActivity() {

    // View Binding
    private var binding: ContentMainBinding? = null
    private var mainBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding way of inflating the root
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding?.root)

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Bottom Navigation
        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)


        val radius = resources.getDimension(R.dimen.roundcorner)
        val navViewBackground = mainBinding?.navView?.background as MaterialShapeDrawable
        navViewBackground.shapeAppearanceModel = navViewBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .setBottomRightCorner(CornerFamily.ROUNDED, radius)
            .build()


        // Drawer
        mainBinding?.navView?.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.drawer_home -> {
                    // handle click
                    true
                }
                R.id.drawer_category -> {
                    startActivity(Intent(applicationContext, CategoryActivity::class.java))
                    true
                }
                R.id.drawer_recurring -> {
                    // handle click
                    true
                }
                else -> {
                    false
                }
            }
        }


        setupActionBar()
    }



    fun setupActionBar() {
        // findViewById<ImageView>(R.id.iv_burger).setOnClickListener {
            if (findViewById<DrawerLayout>(R.id.drawer_layout).isDrawerOpen(GravityCompat.START)) {
                findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            } else {
                findViewById<DrawerLayout>(R.id.drawer_layout).openDrawer(GravityCompat.START)
            }
        // }
    }


//    fun getVisibleFragment(): Fragment? {
//        val fragmentManager: FragmentManager = this@MainActivity.supportFragmentManager
//        val fragments: List<Fragment> = fragmentManager.fragments
//        if (fragments != null) {
//            for (fragment in fragments) {
//                if (fragment != null && fragment.isVisible) return fragment
//            }
//        }
//        return null
//    }

    /**
     * Joins Items adapter & Main activity layout
     */
//    private fun setupListOfDataIntoRecyclerView(
//        employeesList: ArrayList<EmployeeEntity>,
//        employeeDao: EmployeeDao
//    ) {
//
//        if(employeesList.isNotEmpty()) {
//            val itemAdapter = ItemAdapter(employeesList,
//                {
//                    updateId ->
//                    updateRecordDialog(updateId, employeeDao)
//                },
//                {
//                        deleteId ->
//                    deleteRecordAlertDialog(deleteId, employeeDao)
//                }
//
//            )
//            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
//            binding?.rvItemsList?.adapter = itemAdapter
//            binding?.rvItemsList?.visibility = View.VISIBLE
//            binding?.tvNoRecordsAvailable?.visibility = View.GONE
//        } else {
//            binding?.rvItemsList?.visibility = View.GONE
//            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
//        }
//    }


    /* ============================================ *
     |   Implementing the database operations       |
     * ============================================ */


}



