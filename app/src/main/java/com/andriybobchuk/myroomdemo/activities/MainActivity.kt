package com.andriybobchuk.myroomdemo.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.andriybobchuk.myroomdemo.R
//import com.andriybobchuk.myroomdemo.adapters.ItemAdapter
import com.andriybobchuk.myroomdemo.databinding.ActivityMainBinding
import com.andriybobchuk.myroomdemo.databinding.DialogUpdateBinding
import com.andriybobchuk.myroomdemo.room.AccountDao
import com.andriybobchuk.myroomdemo.room.AccountEntity
import com.andriybobchuk.myroomdemo.room.TransactionDao
import com.andriybobchuk.myroomdemo.room.TransactionEntity
import com.andriybobchuk.myroomdemo.util.FinanceApp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.andriybobchuk.myroomdemo.HistoryFragment
import com.andriybobchuk.myroomdemo.MainFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


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

