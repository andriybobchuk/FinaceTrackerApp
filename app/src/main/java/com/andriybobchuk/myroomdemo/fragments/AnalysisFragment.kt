package com.andriybobchuk.myroomdemo.fragments

import android.annotation.SuppressLint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.andriybobchuk.myroomdemo.activities.MainActivity
import com.andriybobchuk.myroomdemo.adapters.ExpenseItemAdapter
import com.andriybobchuk.myroomdemo.adapters.MonthsItemAdapter
import com.andriybobchuk.myroomdemo.databinding.FragmentAnalysisBinding
import com.andriybobchuk.myroomdemo.room.TransactionEntity
import com.andriybobchuk.myroomdemo.util.Constants
import com.andriybobchuk.myroomdemo.util.MyDateConverter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import android.graphics.pdf.PdfDocument.PageInfo
import android.widget.Toast
import com.google.android.material.internal.ViewUtils.getContentView
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.time.format.TextStyle
import java.util.jar.Manifest


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

    var mCurrentMonth = ""
    var mExpenseByCategorySumMap = mutableMapOf<String, String>()
    var mExpenseByAccountSumMap = mutableMapOf<String, String>()
    var mIncomeByCategorySumMap = mutableMapOf<String, String>()
    var mIncomeByAccountSumMap = mutableMapOf<String, String>()

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        val view = binding.root

        //ToDo("Crashes the app")
        // Maybe its null?
        binding.ivBurger.setOnClickListener {
            MainActivity().setupActionBar()
        }


        // Disable nested scrolling
        binding.sv.isNestedScrollingEnabled = false
        binding.rvExpenseList.isNestedScrollingEnabled = false
        binding.rvExpenseAccountList.isNestedScrollingEnabled = false
        binding.rvIncomeList.isNestedScrollingEnabled = false
        binding.rvIncomeAccountList.isNestedScrollingEnabled = false
        prepareDateData()


        // Permissions for saving the pdf, Dex De'Shawn takes care of it:
        Dexter.withActivity(activity)
            .withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object: PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    binding.cvGeneratePdf.setOnClickListener {
                        generatePdf(Constants.getAppPath(requireContext()) + mCurrentMonth + " Report.pdf")
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    TODO("Not yet implemented")
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    TODO("Not yet implemented")
                }

            })
            .check()

        return view
    }


    private fun generatePdf(path: String) {

        if(File(path).exists()) {
            File(path).delete()
        }

        try {
            // General setup
            val document = Document()
            PdfWriter.getInstance(document, FileOutputStream(path))
            document.open()

            // Page setup
            document.pageSize = PageSize.A4
            document.setMargins(0.0f, 0.0f, 60.0f, 10.0f)
            document.addCreationDate()
            document.addAuthor(Constants.DOCUMENT_AUTHOR)
            document.addCreator(Constants.DOCUMENT_CREATOR)

            // Font setup
            val colorAccent = BaseColor(84, 146, 220, 255)
            val fontRegular = BaseFont.createFont("res/font/segoeui.TTF", "UTF-8", BaseFont.EMBEDDED)
            val fontSemiBold = BaseFont.createFont("res/font/segoeuisb.ttf", "UTF-8", BaseFont.EMBEDDED)
            val fontBold = BaseFont.createFont("res/font/segoeuib.ttf", "UTF-8", BaseFont.EMBEDDED)

            // Add some nice caption :)
            val captionStyle = Font(fontRegular, 8.5f, Font.NORMAL, BaseColor.DARK_GRAY)
            addNewItem(document, "Generated by the Ins'n'Outs app :)", Element.ALIGN_RIGHT, captionStyle)
            addLineSpace(document)

            // Add title
            val title = mCurrentMonth + " Report"
            val titleStyle = Font(fontBold, 18.0f, Font.NORMAL, BaseColor.BLACK)
            addNewItem(document, title, Element.ALIGN_CENTER, titleStyle)

            // "Expenses" header1
            val header1Style = Font(fontSemiBold, 16.0f, Font.NORMAL, BaseColor.BLACK)
            addNewItem(document, "Expenses", Element.ALIGN_LEFT, header1Style)

            addLineSeparator(document)
            addLineSpace(document)

            // "By Categories" header2
            val header2Style = Font(fontSemiBold, 10.0f, Font.NORMAL, colorAccent)
            addNewItem(document, "By categories", Element.ALIGN_LEFT, header2Style)

            // "By Categories" header3
            val header3StyleA = Font(fontRegular, 10.0f, Font.NORMAL, BaseColor.BLACK)
            val header3StyleB = Font(fontSemiBold, 10.0f, Font.NORMAL, BaseColor.BLACK)
            mExpenseByCategorySumMap.forEach {
                addDoubleSidedItem(document, it.key, it.value, header3StyleA, header3StyleB)
            }

            addLineSpace(document)
            addNewItem(document, "By accounts", Element.ALIGN_LEFT, header2Style)
            mExpenseByAccountSumMap.forEach {
                addDoubleSidedItem(document, it.key, it.value, header3StyleA, header3StyleB)
            }

            addLineSpace(document)
            addNewItem(document, "Income", Element.ALIGN_LEFT, header1Style)
            addLineSeparator(document)
            addLineSpace(document)
            addNewItem(document, "By categories", Element.ALIGN_LEFT, header2Style)
            mIncomeByCategorySumMap.forEach {
                addDoubleSidedItem(document, it.key, it.value, header3StyleA, header3StyleB)
            }
            addLineSpace(document)
            addNewItem(document, "By accounts", Element.ALIGN_LEFT, header2Style)
            mIncomeByAccountSumMap.forEach {
                addDoubleSidedItem(document, it.key, it.value, header3StyleA, header3StyleB)
            }

            document.close()

            Toast.makeText(
                requireContext(),
                "File $mCurrentMonth Report.pdf has been saved to ${Constants.getAppPath(requireContext())}",
                Toast.LENGTH_LONG
            ).show()

        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                "${e}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text: String, alignment: Int, style: Font) {
        val chunk = Chunk(text, style)
        val paragraph = Paragraph(chunk)
        paragraph.alignment = alignment
        document.add(paragraph)
    }

    @Throws(DocumentException::class)
    private fun addDoubleSidedItem(
        document: Document,
        textOnTheLeft: String,
        textOnTheRight: String,
        leftStyle: Font,
        rightStyle: Font
    ) {
        val chunkLeft = Chunk(textOnTheLeft, leftStyle)
        val chunkRight = Chunk(textOnTheRight, rightStyle)
        val paragraph = Paragraph(chunkLeft)
        paragraph.add(Chunk(VerticalPositionMark()))
        paragraph.add(chunkRight)
        document.add(paragraph)
    }

    @Throws(DocumentException::class)
    private fun addLineSpace(document: Document) {
        document.add(Paragraph("\n"))
    }

    @Throws(DocumentException::class)
    private fun addLineSeparator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor.BLACK
        lineSeparator.lineWidth = 0.25f
        document.add(Chunk(lineSeparator))
    }


    private fun prepareDateData() {

        var transactionList: ArrayList<TransactionEntity> // What we get from the DB
        var dateList = mutableListOf<TransactionEntity>() // Needed to populate the recycler

        // dateSet is needed for:
        // a) Finding all the unique months using set properties
        // b) Later on whe we filter out analysis data by unique month stamps
        val dateSet = mutableSetOf<String>()

        lifecycleScope.launch {
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


                if(dateList.size > 0) {
                    setupDateRecycler(dateList)
                    binding.cvGeneratePdf.visibility = View.VISIBLE
                } else {
                    binding.cvGeneratePdf.visibility = View.GONE
                }
            }
        }
    }


    private fun setupDateRecycler(dateList: MutableList<TransactionEntity>) {

        if(!dateList.isNullOrEmpty()) {

            binding.rvMonthsList.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, true
            )
            binding.rvMonthsList.setHasFixedSize(true)

            val adapter = MonthsItemAdapter(
                requireContext(),
                dateList as ArrayList<TransactionEntity>
            )
            binding.rvMonthsList.adapter = adapter // Attach the adapter to the recyclerView.

            /* ==================================================================================== *
             *                      Setup 4 other DataPreparatory funs using click handling
             * ==================================================================================== */

            adapter.setOnClickListener(object : MonthsItemAdapter.OnClickListener {
                override fun onClick(position: Int, model: TransactionEntity) {

                    // Zeroing the old data maps for the pdf:
                    mExpenseByCategorySumMap = mutableMapOf<String, String>()
                    mExpenseByAccountSumMap = mutableMapOf<String, String>()
                    mIncomeByCategorySumMap = mutableMapOf<String, String>()
                    mIncomeByAccountSumMap = mutableMapOf<String, String>()

                    val requestedMonth = MyDateConverter(model.date).monthString +
                            MyDateConverter(model.date).year

                    mCurrentMonth = (MyDateConverter(model.date).monthString
                    + " "
                    + MyDateConverter(model.date).year)

                    //setupExpenseRecycler(requestedMonth)
                    prepareExpensesByCategoriesData(requestedMonth)
                    prepareExpensesByAccountData(requestedMonth)
                    prepareIncomeByCategoriesData(requestedMonth)
                    prepareIncomeByAccountData(requestedMonth)
                }
            })
        }

        /* ==================================================================================== *
         *                      Setup other recyclers for the default month
         *                      (0-th element in the date recycler)
         * ==================================================================================== */
        val mostRecentMonth =
            MyDateConverter(dateList[0].date).monthString + MyDateConverter(
                dateList[0].date
            ).year

        mCurrentMonth = (MyDateConverter(dateList[0].date).monthString
                + " "
                + MyDateConverter(dateList[0].date).year)

        //setupExpenseRecycler(mostRecentMonth)
        prepareExpensesByCategoriesData(mostRecentMonth)
        prepareExpensesByAccountData(mostRecentMonth)
        prepareIncomeByCategoriesData(mostRecentMonth)
        prepareIncomeByAccountData(mostRecentMonth)
    }

    /** ==================================================================================== *
     *                      EXPENSE
     * ==================================================================================== */

    private fun prepareExpensesByCategoriesData(requiredMonth: String) {

        var transactionList: ArrayList<TransactionEntity>
        var expenseList = mutableListOf<TransactionEntity>()
        val categorySet = mutableSetOf<String>()
        var categorySumMap = mutableMapOf<String, String>()

        lifecycleScope.launch {
            MainFragment.mTransactionDao.fetchAllTransactions().collect { transactionEntityList ->
                transactionList = ArrayList(transactionEntityList)

                // Getting unique expense list for the required date
                transactionList.forEach { transactionEntity ->

                    val currentMonth = MyDateConverter(transactionEntity.date).monthString +
                            MyDateConverter(transactionEntity.date).year

                    if (currentMonth == requiredMonth) {
                        if (transactionEntity.categoryType == Constants.EXPENSE) {
                            categorySet.add(transactionEntity.category)
                            expenseList.add(transactionEntity)
                        }
                    }
                }

                categorySet.forEach { currentCategory ->
                    var currentCategorySum = 0.00
                    var currentCategoryCurrency = ""
                    expenseList.forEach {
                        if(it.category == currentCategory) {
                            currentCategorySum += it.amount.toDouble()
                        }
                        currentCategoryCurrency = it.currency
                    }
                    categorySumMap += Pair(
                        currentCategory,
                        currentCategoryCurrency + " " + "%.2f".format(currentCategorySum)
                    )
                }
//                Toast.makeText(
//                    requireContext(),
//                    "${categorySumMap.size} ${-1}",
//                    Toast.LENGTH_LONG
//                ).show()

                if(categorySumMap.isNotEmpty()) {
                    mExpenseByCategorySumMap = categorySumMap
                    setupExpenseByCategoryRecycler(categorySumMap)
                    binding.tvExpensesByCategoryNoData.visibility = View.GONE
                    binding.rvExpenseList.visibility = View.VISIBLE
                } else {
                    binding.tvExpensesByCategoryNoData.visibility = View.VISIBLE
                    binding.rvExpenseList.visibility = View.GONE
                }
            }
        }
    }

    private fun setupExpenseByCategoryRecycler(categorySumMap: MutableMap<String, String>) {

        binding.rvExpenseList.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvExpenseList.setHasFixedSize(true)

        if (!categorySumMap.isNullOrEmpty()) {
            val adapter = ExpenseItemAdapter(
                requireContext(),
                categorySumMap
            )
            binding.rvExpenseList.adapter =
                adapter // Attach the adapter to the recyclerView.
        }

    }

    private fun prepareExpensesByAccountData(requiredMonth: String) {

        var transactionList: ArrayList<TransactionEntity>
        var expenseList = mutableListOf<TransactionEntity>()
        val accountSet = mutableSetOf<String>()
        var accountSumMap = mutableMapOf<String, String>()

        lifecycleScope.launch {
            MainFragment.mTransactionDao.fetchAllTransactions().collect { transactionEntityList ->
                transactionList = ArrayList(transactionEntityList)

                // Getting unique expense list for the required date
                transactionList.forEach { transactionEntity ->

                    val currentMonth = MyDateConverter(transactionEntity.date).monthString +
                            MyDateConverter(transactionEntity.date).year

                    if (currentMonth == requiredMonth) {
                        if (transactionEntity.categoryType == Constants.EXPENSE) {
                            accountSet.add(transactionEntity.account)
                            expenseList.add(transactionEntity)
                        }
                    }
                }

                accountSet.forEach { currentAccount ->
                    var currentAccountSum = 0.00
                    var currentAccountCurrency = ""
                    expenseList.forEach {
                        if(it.account == currentAccount) {
                            currentAccountSum += it.amount.toDouble()
                        }
                        currentAccountCurrency = it.currency
                    }
                    accountSumMap += Pair(
                        currentAccount,
                        currentAccountCurrency + " " + "%.2f".format(currentAccountSum)
                    )
                }
//                Toast.makeText(
//                    requireContext(),
//                    "${categorySumMap.size} ${-1}",
//                    Toast.LENGTH_LONG
//                ).show()

                if(accountSumMap.isNotEmpty()) {
                    mExpenseByAccountSumMap = accountSumMap
                    setupExpenseByAccountRecycler(accountSumMap)
                    binding.tvExpensesByAccountNoData.visibility = View.GONE
                    binding.rvExpenseAccountList.visibility = View.VISIBLE
                } else {
                    binding.tvExpensesByAccountNoData.visibility = View.VISIBLE
                    binding.rvExpenseAccountList.visibility = View.GONE
                }
            }
        }
    }

    private fun setupExpenseByAccountRecycler(accountSumMap: MutableMap<String, String>) {

        binding.rvExpenseAccountList.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvExpenseAccountList.setHasFixedSize(true)

        if (!accountSumMap.isNullOrEmpty()) {
            val adapter = ExpenseItemAdapter(
                requireContext(),
                accountSumMap
            )
            binding.rvExpenseAccountList.adapter =
                adapter // Attach the adapter to the recyclerView.
        }

    }

    /** ==================================================================================== *
     *                      INCOME
     * ==================================================================================== */

    private fun prepareIncomeByCategoriesData(requiredMonth: String) {

        var transactionList: ArrayList<TransactionEntity>
        var incomeList = mutableListOf<TransactionEntity>()
        val categorySet = mutableSetOf<String>()
        var categorySumMap = mutableMapOf<String, String>()

        lifecycleScope.launch {
            MainFragment.mTransactionDao.fetchAllTransactions().collect { transactionEntityList ->
                transactionList = ArrayList(transactionEntityList)

                // Getting unique income list for the required date
                transactionList.forEach { transactionEntity ->

                    val currentMonth = MyDateConverter(transactionEntity.date).monthString +
                            MyDateConverter(transactionEntity.date).year

                    if (currentMonth == requiredMonth) {
                        if (transactionEntity.categoryType == Constants.INCOME) {
                            categorySet.add(transactionEntity.category)
                            incomeList.add(transactionEntity)
                        }
                    }
                }

                categorySet.forEach { currentCategory ->
                    var currentCategorySum = 0.00
                    var currentCategoryCurrency = ""
                    incomeList.forEach {
                        if(it.category == currentCategory) {
                            currentCategorySum += it.amount.toDouble()
                        }
                        currentCategoryCurrency = it.currency
                    }
                    categorySumMap += Pair(
                        currentCategory,
                        currentCategoryCurrency + " " + "%.2f".format(currentCategorySum)
                    )
                }
//                Toast.makeText(
//                    requireContext(),
//                    "${categorySumMap.size} ${-1}",
//                    Toast.LENGTH_LONG
//                ).show()

                if(categorySumMap.isNotEmpty()) {
                    mIncomeByCategorySumMap = categorySumMap
                    setupIncomeByCategoryRecycler(categorySumMap)
                    binding.tvIncomeByCategoryNoData.visibility = View.GONE
                    binding.rvIncomeList.visibility = View.VISIBLE
                } else {
                    binding.tvIncomeByCategoryNoData.visibility = View.VISIBLE
                    binding.rvIncomeList.visibility = View.GONE
                }
            }
        }
    }


    private fun setupIncomeByCategoryRecycler(categorySumMap: MutableMap<String, String>) {

        binding.rvIncomeList.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvIncomeList.setHasFixedSize(true)

        if (!categorySumMap.isNullOrEmpty()) {
            val adapter = ExpenseItemAdapter(
                requireContext(),
                categorySumMap
            )
            binding.rvIncomeList.adapter =
                adapter // Attach the adapter to the recyclerView.
        }

    }


    private fun prepareIncomeByAccountData(requiredMonth: String) {

        var transactionList: ArrayList<TransactionEntity>
        var incomeList = mutableListOf<TransactionEntity>()
        val accountSet = mutableSetOf<String>()
        var accountSumMap = mutableMapOf<String, String>()

        lifecycleScope.launch {
            MainFragment.mTransactionDao.fetchAllTransactions().collect { transactionEntityList ->
                transactionList = ArrayList(transactionEntityList)

                // Getting unique expense list for the required date
                transactionList.forEach { transactionEntity ->

                    val currentMonth = MyDateConverter(transactionEntity.date).monthString +
                            MyDateConverter(transactionEntity.date).year

                    if (currentMonth == requiredMonth) {
                        if (transactionEntity.categoryType == Constants.INCOME) {
                            accountSet.add(transactionEntity.account)
                            incomeList.add(transactionEntity)
                        }
                    }
                }

                accountSet.forEach { currentAccount ->
                    var currentAccountSum = 0.00
                    var currentAccountCurrency = ""
                    incomeList.forEach {
                        if(it.account == currentAccount) {
                            currentAccountSum += it.amount.toDouble()
                        }
                        currentAccountCurrency = it.currency
                    }
                    accountSumMap += Pair(
                        currentAccount,
                        currentAccountCurrency + " " + "%.2f".format(currentAccountSum)
                    )
                }
//                Toast.makeText(
//                    requireContext(),
//                    "${categorySumMap.size} ${-1}",
//                    Toast.LENGTH_LONG
//                ).show()

                if(accountSumMap.isNotEmpty()) {
                    mIncomeByAccountSumMap = accountSumMap
                    setupIncomeByAccountRecycler(accountSumMap)
                    binding.tvIncomeByAccountNoData.visibility = View.GONE
                    binding.rvIncomeAccountList.visibility = View.VISIBLE
                } else {
                    binding.tvIncomeByAccountNoData.visibility = View.VISIBLE
                    binding.rvIncomeAccountList.visibility = View.GONE
                }
            }
        }
    }

    private fun setupIncomeByAccountRecycler(accountSumMap: MutableMap<String, String>) {

        binding.rvIncomeAccountList.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.rvIncomeAccountList.setHasFixedSize(true)

        if (!accountSumMap.isNullOrEmpty()) {
            val adapter = ExpenseItemAdapter(
                requireContext(),
                accountSumMap
            )
            binding.rvIncomeAccountList.adapter =
                adapter // Attach the adapter to the recyclerView.
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
                            if (it.category == category) {
                                sumInCategory += it.amount.toDouble()
                            }
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

