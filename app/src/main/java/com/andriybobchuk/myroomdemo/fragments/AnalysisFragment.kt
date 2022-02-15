package com.andriybobchuk.myroomdemo.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.andriybobchuk.myroomdemo.activities.MainActivity
import com.andriybobchuk.myroomdemo.adapters.ExpenseItemAdapter
import com.andriybobchuk.myroomdemo.adapters.MonthsItemAdapter
import com.andriybobchuk.myroomdemo.databinding.FragmentAnalysisBinding
import com.andriybobchuk.myroomdemo.room.CategoryEntity
import com.andriybobchuk.myroomdemo.room.TransactionEntity
import com.andriybobchuk.myroomdemo.util.Constants
import com.andriybobchuk.myroomdemo.util.MyDateConverter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AnalysisFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnalysisFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    // For view binding
    private var _binding: FragmentAnalysisBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        val view = binding.root

        //todo
        binding.ivBurger.setOnClickListener {
            MainActivity().setupActionBar()
        }




        setupDateRecycler()
        //setupExpenseRecycler("Apr2022")
        //setupIncomeRecycler()













        return view
    }


    fun prepareDateData() {

        var transactionList: ArrayList<TransactionEntity> // What we get from the DB
        var dateList = mutableListOf<TransactionEntity>() // Needed to populate the recycler

        // dateSet is needed for:
        // a) Finding all the unique months using set properties
        // b) Later on whe we filter out analysis data by unique month stamps
        val dateSet = mutableSetOf<String>()

        runBlocking {
            MainFragment.mTransactionDao.fetchAllTransactions().collect { list ->
                transactionList = ArrayList(list) // cast List to ArrayList type

                // Take all TransactionEntities and make a list of date strings out of it
                var currentDate: String
                transactionList.forEach { transactionEntity ->

                    currentDate = MyDateConverter(transactionEntity.date).monthString +
                            MyDateConverter(transactionEntity.date).year

                    if (!dateSet.contains(currentDate)) {
                        dateSet.add(currentDate)
                        dateList.add(transactionEntity)
                    }
                }
                dateList.sortByDescending { MyDateConverter(it.date).date }


            }
        }
    }



    private fun setupDateRecycler() {

        var transactionList: ArrayList<TransactionEntity>? // Initial list we get
        val setOfStringMonths = mutableSetOf<String>() // Helper set to filter all copies out
        val listOfMonths = mutableListOf<TransactionEntity>() // Thingy we'll pass to recycler

        lifecycleScope.launch {
            MainFragment.mTransactionDao.fetchAllTransactions().collect { itr ->
                transactionList = ArrayList(itr)

                for (element in transactionList!!) {
                    if (!setOfStringMonths.contains(
                            MyDateConverter(element.date).monthString +
                                    MyDateConverter(element.date).year
                        )
                    ) {
                        setOfStringMonths.add(
                            MyDateConverter(element.date).monthString +
                                    MyDateConverter(element.date).year
                        )
                        listOfMonths.add(element)
                    }
                }
                listOfMonths.sortByDescending { MyDateConverter(it.date).date }

                binding.rvMonthsList.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL, true
                )
                binding.rvMonthsList.setHasFixedSize(true)



                if (transactionList != null) {
                    val adapter = MonthsItemAdapter(
                        requireContext(),
                        listOfMonths as ArrayList<TransactionEntity>
                    )
                    binding.rvMonthsList.adapter =
                        adapter // Attach the adapter to the recyclerView.


                    ////////////////////END OF DATE RECYCLER RESPONSIBILITIES///////////////////////////////////////


                    adapter.setOnClickListener(object : MonthsItemAdapter.OnClickListener {
                        override fun onClick(position: Int, model: TransactionEntity) {

                            val requestedMonth = MyDateConverter(model.date).monthString +
                                    MyDateConverter(model.date).year

                            setupExpenseRecycler(requestedMonth)
                        }
                    })


                    // Setup other recyclers for the default month
                    // (0-th element in the date recycler)
                    val mostRecentMonth =
                        MyDateConverter(listOfMonths[0].date).monthString + MyDateConverter(
                            listOfMonths[0].date
                        ).year

                    setupExpenseRecycler(mostRecentMonth)
                }

            }
        }


    }

    private fun setupExpenseRecycler(requiredMonth: String) {
        var transactionList: ArrayList<TransactionEntity>? // Initial list we get
        val setOfCategories = mutableSetOf<String>() // Helper set to filter all copies out
        val mapCategoryToSum = mutableMapOf<String, String>()
        var categoryList: ArrayList<CategoryEntity>?

        lifecycleScope.launch {
            MainFragment.mTransactionDao.fetchAllTransactions().collect { itr ->
                transactionList = ArrayList(itr)


                // Filtering stage to get data only for the needed required month
                transactionList!!.forEach {
                    val currentMonth = MyDateConverter(it.date).monthString +
                            MyDateConverter(it.date).year
                    if (currentMonth == requiredMonth) {
                        if (!setOfCategories.contains(it.category)) {
                            setOfCategories.add(it.category)
                        }
                    }
                }

                // Filtering out only expenses +
                // Summing stage to output the total amount of money in each category
                setOfCategories.forEach { category ->
                    MainFragment.mCategoryDao.fetchCategoryByName(category).collect {
                        if(it.name == category) {
                            if(it.type == Constants.EXPENSE) {
                                var sumInCategory: Double = 0.00
                                var currencyOfTheCategory: String = ""
                                transactionList!!.forEach {

                                    val currentMonth = MyDateConverter(it.date).monthString +
                                            MyDateConverter(it.date).year

                                    if (currentMonth == requiredMonth) {
                                        if (it.category == category) { sumInCategory += it.amount.toDouble() }
                                        currencyOfTheCategory = it.currency
                                    }
                                }

                                // We will pass a map of category - sum to the recycler adapter
                                mapCategoryToSum += Pair(
                                    category,
                                    currencyOfTheCategory + " " + "%.2f".format(sumInCategory)
                                )
                            }
                        }
                    }
                }

                binding.rvExpenseList.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL, false
                )
                binding.rvExpenseList.setHasFixedSize(true)

                if (transactionList != null) {
                    val adapter = ExpenseItemAdapter(
                        requireContext(),
                        mapCategoryToSum
                    )
                    binding.rvExpenseList.adapter =
                        adapter // Attach the adapter to the recyclerView.
                }

            }
        }
    }


    private fun setupExpenseByAccountRecycler(requiredMonth: String) {
        var transactionList: ArrayList<TransactionEntity>? // Initial list we get
        val setOfCategories = mutableSetOf<String>() // Helper set to filter all copies out
        val mapCategoryToSum = mutableMapOf<String, String>()

        lifecycleScope.launch {
            MainFragment.mTransactionDao.fetchAllTransactions().collect { itr ->
                transactionList = ArrayList(itr)


                // Filtering stage to get data only for the needed required month
                transactionList!!.forEach {
                    val currentMonth = MyDateConverter(it.date).monthString +
                            MyDateConverter(it.date).year
                    if (currentMonth == requiredMonth) {
                        if (!setOfCategories.contains(it.category)) {
                            setOfCategories.add(it.category)
                        }
                    }
                }

                // Summing stage to output the total amount of money in each category
                setOfCategories.forEach { category ->
                    var sumInCategory: Double = 0.00
                    var currencyOfTheCategory: String = ""
                    transactionList!!.forEach {

                        val currentMonth = MyDateConverter(it.date).monthString +
                                MyDateConverter(it.date).year

                        if (currentMonth == requiredMonth) {
                            if (it.category == category) { sumInCategory += it.amount.toDouble() }
                            currencyOfTheCategory = it.currency
                        }
                    }

                    // We will pass a map of category - sum to the recycler adapter
                    mapCategoryToSum += Pair(
                        category,
                        currencyOfTheCategory + " " + "%.2f".format(sumInCategory)
                    )
                }

                binding.rvExpenseList.layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL, false
                )
                binding.rvExpenseList.setHasFixedSize(true)

                if (transactionList != null) {
                    val adapter = ExpenseItemAdapter(
                        requireContext(),
                        mapCategoryToSum
                    )
                    binding.rvExpenseList.adapter =
                        adapter // Attach the adapter to the recyclerView.
                }

            }
        }
    }

    private fun setupIncomeRecycler() {
        //TODO("Not yet implemented")
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AnalysisFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AnalysisFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}