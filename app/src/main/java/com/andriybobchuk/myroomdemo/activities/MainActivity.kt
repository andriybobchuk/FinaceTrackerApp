package com.andriybobchuk.myroomdemo.activities


import android.app.PendingIntent.getActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.andriybobhcuk.manageux.activities.BaseActivity




class MainActivity : BaseActivity() {

    // View Binding
    private var binding: ContentMainBinding? = null
    private var mainBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding way of inflating the root
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding?.root)

        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)


//        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.mainFragment -> {
//                    findViewById<TextView>(R.id.tv_action_bar_title).text = "MAIN or null"
//                }
//                R.id.historyFragment -> {
//                    findViewById<TextView>(R.id.tv_action_bar_title).text = "HIS or null"
//                } else -> {
//                    findViewById<TextView>(R.id.tv_action_bar_title).text = "MAIN or null"
//                }
//            }
//            false
//        }
//        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        setupActionBar()

    }

    private fun setupActionBar() {

        //findViewById<TextView>(R.id.tv_action_bar_title).text = "${getVisibleFragment()} or null"



//        val currentFragment = supportFragmentManager.fragments.last()?.getChildFragmentManager()?.getFragments()?.get(0)
//        if (currentFragment is MainFragment) {
//            findViewById<TextView>(R.id.tv_action_bar_title).text = "main or null"
//        } else {
//            findViewById<TextView>(R.id.tv_action_bar_title).text = "history or null"
//
//        }

        findViewById<ImageView>(R.id.iv_burger).setOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        if(findViewById<DrawerLayout>(R.id.drawer_layout).isDrawerOpen(GravityCompat.START)) {
            findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        } else {
            findViewById<DrawerLayout>(R.id.drawer_layout).openDrawer(GravityCompat.START)
        }
    }

    fun getVisibleFragment(): Fragment? {
        val fragmentManager: FragmentManager = this@MainActivity.supportFragmentManager
        val fragments: List<Fragment> = fragmentManager.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible) return fragment
            }
        }
        return null
    }

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



