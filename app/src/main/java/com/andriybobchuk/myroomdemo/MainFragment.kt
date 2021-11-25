package com.andriybobchuk.myroomdemo

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.andriybobchuk.myroomdemo.databinding.DialogUpdateBinding
import com.andriybobchuk.myroomdemo.databinding.FragmentMainBinding
import com.andriybobchuk.myroomdemo.room.AccountDao
import com.andriybobchuk.myroomdemo.room.AccountEntity
import com.andriybobchuk.myroomdemo.room.TransactionDao
import com.andriybobchuk.myroomdemo.room.TransactionEntity
import kotlinx.coroutines.launch
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build

import androidx.appcompat.app.AppCompatActivity

import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner

import androidx.lifecycle.lifecycleScope
import com.andriybobchuk.myroomdemo.R
//import com.andriybobchuk.myroomdemo.adapters.ItemAdapter
import com.andriybobchuk.myroomdemo.databinding.ActivityMainBinding

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.andriybobchuk.myroomdemo.HistoryFragment
import com.andriybobchuk.myroomdemo.MainFragment
import com.andriybobchuk.myroomdemo.adapters.AccountItemAdapter
import com.andriybobchuk.myroomdemo.adapters.TransactionItemAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * View Binding example with a fragment that uses the traditional constructor and [onCreateView] for
 * inflation.
 */
class MainFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // For view binding
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        //binding?.tvName?.text = "Andrii"



        var accountList: ArrayList<AccountEntity>? = null
        val accountDao = (activity?.application!! as FinanceApp).db.accountDao()

        lifecycleScope.launch {
            accountDao.fetchAllAccounts().collect { itr ->
                accountList = ArrayList(itr)
                populateAccountListToUI(ArrayList(itr))
            }
        }

    }


    /**
     * with fragments, the layout is inflated in onCreateView().
     * Implement onCreateView() by inflating the view, setting the value of _binding, and returning the root view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root


//        binding?.btnAdd.setOnClickListener {
//            Toast.makeText(activity, "ew", Toast.LENGTH_LONG).show()
//        }
//        binding?.btnAdd?.text = "Andrii"


        var accountsArrayAdapter: ArrayAdapter<String>? = null


        // TODO application - activity?.application!!
        val accountDao = (activity?.application!! as FinanceApp).db.accountDao()
        binding?.btnAdd?.setOnClickListener {
            accountsArrayAdapter?.clear()
            addRecord(accountDao)
        }

        val transactionDao = (activity?.application!! as FinanceApp).db.transactionDao()
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


        // TODO: this - activity?.applicationContext!!
        val categoriesSpinner = binding?.sCategory
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            activity?.applicationContext!!,
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
            activity?.applicationContext!!,
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
            activity?.applicationContext!!,
            R.array.account_types_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            accountTypesSpinner?.adapter = adapter
        }
        /**
         * I have declared an ArrayList of Strings called accountsArray.
         * Inside a lifecycleScope.launch {} I fetch a list of all the accounts via my data access object and add them to an arrayList
         * Those strings I get from a local database
         */


        val accountsArray = arrayListOf<String>()
        lifecycleScope.launch {
            accountDao.fetchAllAccounts().collect { itr ->
                val list = ArrayList(itr)

                list.forEach {
                    accountsArray.add(it.name)
                }
                accountsArrayAdapter?.notifyDataSetChanged()
            }
        }
        accountsArrayAdapter = ArrayAdapter<String>(
            activity?.applicationContext!!,
            R.layout.spinner_item, accountsArray
        )

        accountsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val accountSpinner = binding?.sAccount
        accountSpinner?.adapter = accountsArrayAdapter

        accountSpinner?.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


        // OnClickListener is set to the button for launching the DatePicker Dialog.
        binding?.btnTransactionDate?.setOnClickListener { view ->
            clickDataPicker(view)
        }













        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun addRecord(accountDao: AccountDao) {
        val name = binding?.etName?.text.toString()
        val currency = binding?.sCurrency?.selectedItem.toString()
        val type = binding?.sType?.selectedItem.toString()
        val balance = binding?.etBalance?.text.toString()

        if (name.isNotEmpty() && currency.isNotEmpty() && type.isNotEmpty() && balance.isNotEmpty()) {

            // As database data insertion should be done not on the main thread we launch
            //  it on the coroutine
            lifecycleScope.launch {
                accountDao.insert(
                    AccountEntity(
                        name = name,
                        type = type,
                        currency = currency,
                        balance = balance
                    )
                )
                //Toast.makeText(applicationContext, "Record saved!", Toast.LENGTH_LONG).show()
                Toast.makeText(activity, "Record saved!", Toast.LENGTH_LONG).show()

                // Clearing the text fields
                binding?.etName?.text?.clear()
                binding?.etBalance?.text?.clear()
            }


        } else {
            Toast.makeText(activity, "Fill all of the fields!", Toast.LENGTH_LONG).show()

        }
    }

    private fun addRecord(transactionDao: TransactionDao) {
        val date = binding?.btnTransactionDate?.text.toString()
        val amount = binding?.etAmount?.text.toString()
        val category = binding?.sCategory?.selectedItem.toString()
        val account = binding?.sAccount?.selectedItem.toString()
        val description = binding?.etDescription?.text.toString()


        if (date.isNotEmpty()
            && amount.isNotEmpty()
            && category.isNotEmpty()
            && account.isNotEmpty()
            && description.isNotEmpty()
        ) {

            // As database data insertion should be done not on the main thread we launch
            //  it on the coroutine
            lifecycleScope.launch {
                transactionDao.insert(
                    TransactionEntity(
                        date = date,
                        amount = amount,
                        category = category,
                        account = account,
                        description = description
                    )
                )
                Toast.makeText(activity, "Record saved!", Toast.LENGTH_LONG).show()

                // Clearing the text fields
                //binding?.etTransactionDate?.text?.clear()
                binding?.etAmount?.text?.clear()
                binding?.etDescription?.text?.clear()
            }


        } else {
            Toast.makeText(activity, "Fill all of the fields!", Toast.LENGTH_LONG).show()

        }
    }

    private fun updateRecordDialog(id: Int, accountDao: AccountDao) {
        // TODO: activity?.applicationContext!!
        val updateDialog = Dialog(activity?.applicationContext!!, R.style.Theme_Dialog)
        updateDialog.setCancelable(false) // You cannot click away from it

        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        // Populate the fields in this edit dialog with old values from the db
        // Do it on background thread
        lifecycleScope.launch {
            accountDao.fetchAccountById(id).collect {
                if (it != null) {
                    binding.etUpdateName.setText(it.name)
                    binding.etUpdateEmailId.setText(it.currency)
                }
            }
        }

        binding.tvUpdate.setOnClickListener {
            val name = binding.etUpdateName.text.toString()
            val email = binding.etUpdateEmailId.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                lifecycleScope.launch {
                    accountDao.update(AccountEntity(id, name, email))
                    Toast.makeText(activity, "Changes saved!", Toast.LENGTH_LONG)
                        .show()
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(activity, "Fill all of the fields!", Toast.LENGTH_LONG)
                    .show()
            }
        }

        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()
    }


    private fun deleteRecordAlertDialog(id: Int, accountDao: AccountDao) {
        val builder = AlertDialog.Builder(activity?.applicationContext!!)
        builder.setTitle("Delete Record")
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                accountDao.delete(AccountEntity(id))
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No") { dialogInterface, _ ->
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

        val c = Calendar.getInstance()

        val year =
            c.get(Calendar.YEAR) // Returns the value of the given calendar field. This indicates YEAR
        val month = c.get(Calendar.MONTH) // This indicates the Month
        val day = c.get(Calendar.DAY_OF_MONTH) // This indicates the Day


        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->


                //val sdf = SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH)
                //val selectedDate = "$dayOfMonth/${year + 1}/$year"

                var selectedDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                } else {
                    TODO("VERSION.SDK_INT < O")
                }

                var formatter = DateTimeFormatter.ofPattern("d MMM yyyy")
                var formattedDate = selectedDate.format(formatter)

                // Selected date it set to the TextView to make it visible to user.
                binding?.btnTransactionDate?.text = formattedDate


            },
            year,
            month,
            day
        )

//
        dpd.show() // It is used to show the datePicker Dialog.
    }


    /**
     * A function to populate the result of BOARDS list in the UI i.e in the recyclerView.
     */
    fun populateAccountListToUI(accountList: ArrayList<AccountEntity>) {

        if (accountList.size > 0) {

//            binding.rvTransactionsList.visibility = View.VISIBLE
//            binding.tvNoTransactionsAvailable.visibility = View.GONE
//            binding.ivNoTransactionsAvailable.visibility = View.GONE

            binding.rvAccountList.layoutManager = LinearLayoutManager(requireContext())
            binding.rvAccountList.setHasFixedSize(true)

            // Create an instance of BoardItemsAdapter and pass the boardList to it.
            // TODO: I removed "this"
            val adapter = AccountItemAdapter(accountList)
            binding.rvAccountList.adapter = adapter // Attach the adapter to the recyclerView.

        } else {
//            binding.rvTransactionsList.visibility = View.GONE
//            binding.tvNoTransactionsAvailable.visibility = View.VISIBLE
//            binding.ivNoTransactionsAvailable.visibility = View.VISIBLE
        }
    }


}