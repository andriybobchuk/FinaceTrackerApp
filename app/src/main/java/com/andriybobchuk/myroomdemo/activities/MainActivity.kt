package com.andriybobchuk.myroomdemo.activities


import android.app.PendingIntent.getActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.andriybobchuk.myroomdemo.databinding.ActivityMainBinding
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.andriybobchuk.myroomdemo.MainFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.FragmentTransaction
import com.andriybobchuk.myroomdemo.R
import com.andriybobhcuk.manageux.activities.BaseActivity










class MainActivity : BaseActivity() {

    // View Binding
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding way of inflating the root
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)


        setupActionBar()
    }

    fun setupActionBar() {


        val currentFragment = supportFragmentManager.fragments.last()

        if (currentFragment is MainFragment) {
            binding?.tvActionBarTitle?.text = "Main"
        } else {
            binding?.tvActionBarTitle?.text = "History"
        }


        // todo
//        toolbar_main_activity.setNavigationOnClickListener {
//            toggleDrawer()
//        }
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

