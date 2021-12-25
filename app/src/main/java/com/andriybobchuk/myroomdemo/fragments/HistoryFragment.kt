package com.andriybobchuk.myroomdemo.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.andriybobchuk.myroomdemo.adapters.TransactionItemAdapter
import com.andriybobchuk.myroomdemo.databinding.FragmentHistoryBinding
import com.andriybobchuk.myroomdemo.databinding.FragmentMainBinding
import com.andriybobchuk.myroomdemo.room.CategoryEntity
import com.andriybobchuk.myroomdemo.room.TransactionEntity
import com.andriybobchuk.myroomdemo.util.FinanceApp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // For view binding
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val view = binding.root


        //val transactionArray = arrayListOf<String>()

        var transactionsList: ArrayList<TransactionEntity>? = null
        val transactionDao = (activity?.application!! as FinanceApp).db.transactionDao()

        lifecycleScope.launch {

            transactionDao.fetchAllTransactions().collect { itr ->

                MainFragment.mCategoryDao.fetchAllCategories().collect {

                    var sortedList = ArrayList(itr).sortedWith(compareByDescending { SimpleDateFormat("d MMM yyyy").parse(it.date) })

                    populateTransactionListToUI(ArrayList(sortedList), ArrayList(it))
                }
            }
        }


            //Toast.makeText(activity, "yes trans", Toast.LENGTH_LONG).show()





        return view
    }









    /**
     * A function to populate the result of BOARDS list in the UI i.e in the recyclerView.
     */
    fun populateTransactionListToUI(transactionList: ArrayList<TransactionEntity>, categoryList: ArrayList<CategoryEntity>) {

        if (transactionList.size > 0) {

            binding.rvTransactionsList.visibility = View.VISIBLE
            binding.tvNoTransactionsAvailable.visibility = View.GONE
            binding.ivNoTransactionsAvailable.visibility = View.GONE

            binding.rvTransactionsList.layoutManager = LinearLayoutManager(requireContext())
            binding.rvTransactionsList.setHasFixedSize(true)

            // Create an instance of BoardItemsAdapter and pass the boardList to it.
            val adapter = TransactionItemAdapter(transactionList, categoryList)
            binding.rvTransactionsList.adapter = adapter // Attach the adapter to the recyclerView.

        } else {
            binding.rvTransactionsList.visibility = View.GONE
            binding.tvNoTransactionsAvailable.visibility = View.VISIBLE
            binding.ivNoTransactionsAvailable.visibility = View.VISIBLE
        }
    }











    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}