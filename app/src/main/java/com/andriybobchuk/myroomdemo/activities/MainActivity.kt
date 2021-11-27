package com.andriybobchuk.myroomdemo.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andriybobchuk.myroomdemo.databinding.ActivityMainBinding
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.andriybobchuk.myroomdemo.MainFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.FragmentTransaction
import com.andriybobchuk.myroomdemo.R


class MainActivity : AppCompatActivity() {

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

//        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.nav_host_fragment, MainFragment())
//        ft.commit()


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

