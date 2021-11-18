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








class MainActivity : AppCompatActivity() {

    // View Binding
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding way of inflating the root
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        var accountsArrayAdapter: ArrayAdapter<String>? = null


        val accountDao = (application as FinanceApp).db.accountDao()
        binding?.btnAdd?.setOnClickListener {
            accountsArrayAdapter?.clear()
            addRecord(accountDao)
        }

        val transactionDao = (application as FinanceApp).db.transactionDao()
        binding?.btnAddTransaction?.setOnClickListener {
            addRecord(transactionDao)
        }


        // Coroutine for the loading the data...
//       lifecycleScope.launch {
//           employeeDao.fetchAllEmployees().collect {
//               val list = ArrayList(it)
//               setupListOfDataIntoRecyclerView(list, employeeDao)
//           }
//       }


        val categoriesSpinner = binding?.sCategory
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.transaction_categories_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            categoriesSpinner?.adapter = adapter
        }

        val currenciesSpinner = binding?.sCurrency
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.currencies_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            currenciesSpinner?.adapter = adapter
        }

        val accountTypesSpinner = binding?.sType
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.account_types_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            accountTypesSpinner?.adapter = adapter
        }


        val accountsArray = arrayListOf<String>()
        lifecycleScope.launch {
            accountDao.fetchAllAccounts().collect { itr ->
                val list = ArrayList(itr)

                list.forEach {
                    accountsArray.add(it.name)
                }
            }
        }
        accountsArrayAdapter = ArrayAdapter<String>(
            this,
            R.layout.spinner_item, accountsArray)
        // Specify the layout to use when the list of choices appears
        accountsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner

        val accountSpinner = binding?.sAccount
        accountsArrayAdapter.notifyDataSetChanged();


        accountSpinner?.adapter = accountsArrayAdapter



        accountSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

        }



        // OnClickListener is set to the button for launching the DatePicker Dialog.
        binding?.btnTransactionDate?.setOnClickListener { view ->
            clickDataPicker(view)
        }





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

    fun addRecord(accountDao: AccountDao) {
        val name = binding?.etName?.text.toString()
        val currency = binding?.sCurrency?.selectedItem.toString()
        val type = binding?.sType?.selectedItem.toString()
        val balance = binding?.etBalance?.text.toString()

        if(name.isNotEmpty() && currency.isNotEmpty() && type.isNotEmpty() && balance.isNotEmpty()) {

            // As database data insertion should be done not on the main thread we launch
            //  it on the coroutine
            lifecycleScope.launch {
                accountDao.insert(AccountEntity(
                    name = name,
                    type = type,
                    currency = currency,
                    balance = balance
                ))
                Toast.makeText(applicationContext, "Record saved!", Toast.LENGTH_LONG).show()

                // Clearing the text fields
                binding?.etName?.text?.clear()
                binding?.etBalance?.text?.clear()
            }


        } else {
            Toast.makeText(applicationContext, "Fill all of the fields!", Toast.LENGTH_LONG).show()

        }
    }

    fun addRecord(transactionDao: TransactionDao) {
        val date = binding?.etTransactionDate?.text.toString()
        val amount = binding?.etAmount?.text.toString()
        val category = binding?.sCategory?.selectedItem.toString()
        val account = binding?.sAccount?.selectedItem.toString()
        val description = binding?.etDescription?.text.toString()


        if(date.isNotEmpty()
            && amount.isNotEmpty()
            && category.isNotEmpty()
            && account.isNotEmpty()
            && description.isNotEmpty()) {

            // As database data insertion should be done not on the main thread we launch
            //  it on the coroutine
            lifecycleScope.launch {
                transactionDao.insert(TransactionEntity(
                    date = date,
                    amount = amount,
                    category = category,
                    account = account,
                    description = description
                )
                )
                Toast.makeText(applicationContext, "Record saved!", Toast.LENGTH_LONG).show()

                // Clearing the text fields
                binding?.etTransactionDate?.text?.clear()
                binding?.etAmount?.text?.clear()
                binding?.etDescription?.text?.clear()
            }


        } else {
            Toast.makeText(applicationContext, "Fill all of the fields!", Toast.LENGTH_LONG).show()

        }
    }

    private fun updateRecordDialog(id: Int, accountDao: AccountDao) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false) // You cannot click away from it

        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        // Populate the fields in this edit dialog with old values from the db
        // Do it on background thread
        lifecycleScope.launch {
            accountDao.fetchAccountById(id).collect {
                if(it != null) {
                    binding.etUpdateName.setText(it.name)
                    binding.etUpdateEmailId.setText(it.currency)
                }
            }
        }

        binding.tvUpdate.setOnClickListener {
            val name = binding.etUpdateName.text.toString()
            val email = binding.etUpdateEmailId.text.toString()

            if(name.isNotEmpty() && email.isNotEmpty()) {
                lifecycleScope.launch {
                    accountDao.update(AccountEntity(id, name, email))
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


    private fun deleteRecordAlertDialog(id:Int, accountDao: AccountDao) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setPositiveButton("Yes") { dialogInterface, _->
            lifecycleScope.launch {
                accountDao.delete(AccountEntity(id))
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

    /**
     * The function to show the DatePicker Dialog.
     */
    fun clickDataPicker(view: View) {
        /**
         * This Gets a calendar using the default time zone and locale.
         * The calender returned is based on the current time
         * in the default time zone with the default.
         */
        val c = Calendar.getInstance()
        val year =
            c.get(Calendar.YEAR) // Returns the value of the given calendar field. This indicates YEAR
        val month = c.get(Calendar.MONTH) // This indicates the Month
        val day = c.get(Calendar.DAY_OF_MONTH) // This indicates the Day

        /**
         * Creates a new date picker dialog for the specified date using the parent
         * context's default date picker dialog theme.
         */
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                /**
                 * The listener used to indicate the user has finished selecting a date.
                 */

                /**
                 * The listener used to indicate the user has finished selecting a date.
                 */

                /**
                 *Here the selected date is set into format i.e : day/Month/Year
                 * And the month is counted in java is 0 to 11 so we need to add +1 so it can be as selected.
                 * */
                /**
                 *Here the selected date is set into format i.e : day/Month/Year
                 * And the month is counted in java is 0 to 11 so we need to add +1 so it can be as selected.
                 * */
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"

                // Selected date it set to the TextView to make it visible to user.
                binding?.btnTransactionDate?.text = selectedDate

                /**
                 * Here we have taken an instance of Date Formatter as it will format our
                 * selected date in the format which we pass it as an parameter and Locale.
                 * Here I have passed the format as dd/MM/yyyy.
                 */
                /**
                 * Here we have taken an instance of Date Formatter as it will format our
                 * selected date in the format which we pass it as an parameter and Locale.
                 * Here I have passed the format as dd/MM/yyyy.
                 */
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

            },
            year,
            month,
            day
        )

//        /**
//         * Sets the maximal date supported by this in
//         * milliseconds since January 1, 1970 00:00:00 in time zone.
//         *
//         * @param maxDate The maximal supported date.
//         */
//        // 86400000 is milliseconds of 24 Hours. Which is used to restrict the user to select today and future day.
//        dpd.datePicker.setMaxDate(Date().time - 86400000)
        dpd.show() // It is used to show the datePicker Dialog.
    }


}

