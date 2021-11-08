package com.andriybobchuk.myroomdemo

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.andriybobchuk.myroomdemo.databinding.ActivityMainBinding
import com.andriybobchuk.myroomdemo.databinding.DialogUpdateBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    // View Binding
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding way of inflating the root
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        val employeeDao = (application as EmployeeApp).db.employeeDao()
        binding?.btnAdd?.setOnClickListener {
            addRecord(employeeDao)
        }

        // Coroutine for the loading the data...
       lifecycleScope.launch {
           employeeDao.fetchAllEmployees().collect {
               val list = ArrayList(it)
               setupListOfDataIntoRecyclerView(list, employeeDao)
           }
       }


    }


    /**
     * Joins Items adapter & Main activity layout
      */
    private fun setupListOfDataIntoRecyclerView(
        employeesList: ArrayList<EmployeeEntity>,
        employeeDao: EmployeeDao) {

        if(employeesList.isNotEmpty()) {
            val itemAdapter = ItemAdapter(employeesList,
                {
                    updateId ->
                    updateRecordDialog(updateId, employeeDao)
                },
                {
                        deleteId ->
                    deleteRecordAlertDialog(deleteId, employeeDao)
                }

            )
            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemsList?.adapter = itemAdapter
            binding?.rvItemsList?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
        } else {
            binding?.rvItemsList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }


    /* ============================================ *
     |   Implementing the database operations       |
     * ============================================ */

    fun addRecord(employeeDao: EmployeeDao) {
        val name = binding?.etName?.text.toString()
        val email = binding?.etEmailId?.text.toString()

        if(name.isNotEmpty() && email.isNotEmpty()) {

            // As database data insertion should be done not on the main thread we launch
            //  it on the coroutine
            lifecycleScope.launch {
                employeeDao.insert(EmployeeEntity(name = name, email = email))
                Toast.makeText(applicationContext, "Record saved!", Toast.LENGTH_LONG).show()

                // Clearing the text fields
                binding?.etName?.text?.clear()
                binding?.etEmailId?.text?.clear()
            }
        } else {
            Toast.makeText(applicationContext, "Fill all of the fields!", Toast.LENGTH_LONG).show()

        }
    }

    private fun updateRecordDialog(id: Int, employeeDao: EmployeeDao) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false) // You cannot click away from it

        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        // Populate the fields in this edit dialog with old values from the db
        // Do it on background thread
        lifecycleScope.launch {
            employeeDao.fetchEmployeeById(id).collect {
                if(it != null) {
                    binding.etUpdateName.setText(it.name)
                    binding.etUpdateEmailId.setText(it.email)
                }
            }
        }

        binding.tvUpdate.setOnClickListener {
            val name = binding.etUpdateName.text.toString()
            val email = binding.etUpdateEmailId.text.toString()

            if(name.isNotEmpty() && email.isNotEmpty()) {
                lifecycleScope.launch {
                    employeeDao.update(EmployeeEntity(id, name, email))
                    Toast.makeText(applicationContext, "Changes saved!", Toast.LENGTH_LONG)
                        .show()
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(applicationContext, "Fill all of the fields!", Toast.LENGTH_LONG)
                    .show()
            }
        }

        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()
    }


    private fun deleteRecordAlertDialog(id:Int, employeeDao: EmployeeDao) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setPositiveButton("Yes") { dialogInterface, _->
            lifecycleScope.launch {
                employeeDao.delete(EmployeeEntity(id))
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No") { dialogInterface, _->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}