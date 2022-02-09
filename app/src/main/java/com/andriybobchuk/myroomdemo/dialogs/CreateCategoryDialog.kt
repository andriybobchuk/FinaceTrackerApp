package com.andriybobchuk.myroomdemo.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Toast
import com.andriybobchuk.myroomdemo.R
import com.andriybobchuk.myroomdemo.databinding.DialogCreateCategoryBinding
import com.andriybobchuk.myroomdemo.room.CategoryEntity
import com.andriybobchuk.myroomdemo.util.Constants

class CreateCategoryDialog(
    val categoryActivityContext: Context
) : Dialog(categoryActivityContext) {

    init {
        setCancelable(true)
    }

    var binding: DialogCreateCategoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DialogCreateCategoryBinding.inflate(
            LayoutInflater.from(context)
        )
        setContentView(binding!!.root)

        window?.setLayout(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

       // createCategory()
//        //val accountDao: AccountDao? = null
//        //var accountDao = (mainActivityContext as FinanceApp).db.accountDao()
//        binding?.btnCreate?.setOnClickListener {
//
//            val name = binding?.etName?.text.toString()
//            val type = binding?.sType?.selectedItem.toString()
//
//
//            if (name.isNotEmpty()) {
//                var category = CategoryEntity(
//                    name = name,
//                    type = type,
//                )
//                //MainFragment().addRecord(accountDao, account)
//                dismiss()
//
//            } else {
//                Toast.makeText(context, "Fill all of the fields!", Toast.LENGTH_LONG).show()
//            }
//
//            // Clearing the text fields
//            binding?.etName?.text?.clear()
//        }

        val accountTypesSpinner = binding?.sType
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            context,
            R.array.category_types_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            accountTypesSpinner?.adapter = adapter
        }
    }



    private fun createCategory() {

        val name = binding?.etName?.text.toString()
        val type = binding?.sType?.selectedItem.toString()
        val icon: Int

        if (name.isNotEmpty() && type.isNotEmpty()) {

            if (type == Constants.EXPENSE) {
                icon = R.drawable.ic_other_income
            } else {
                icon = R.drawable.ic_other_expense
            }

            var category = CategoryEntity(
                name = name,
                type = type,
                icon = icon
            )

           // MainFragment.mCategoryDao.insert(category)

//            lifecycleScope.launch {
//                MainFragment.mCategoryDao.insert(category)
//            }

        } else {
            Toast.makeText(context, "Fill all of the fields!", Toast.LENGTH_LONG).show()
        }

        // Clearing the text fields
        binding?.etName?.text?.clear()
    }


}