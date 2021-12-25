package com.andriybobchuk.myroomdemo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.andriybobchuk.myroomdemo.R
import com.andriybobchuk.myroomdemo.databinding.ActivityCategoryBinding
import com.andriybobchuk.myroomdemo.fragments.MainFragment
import com.andriybobchuk.myroomdemo.room.CategoryEntity
import com.andriybobchuk.myroomdemo.util.Constants
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {

    private var binding: ActivityCategoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding way of inflating the root
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnFinish?.setOnClickListener {
            createAccount()
            finish()
        }

        val categoryTypesSpinner = binding?.sType
        ArrayAdapter.createFromResource(
            applicationContext,
            R.array.category_types_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categoryTypesSpinner?.adapter = adapter
        }
    }


    private fun createAccount() {

        val name = binding?.etName?.text.toString()
        val type = binding?.sType?.selectedItem.toString()
        val icon: Int

        if (name.isNotEmpty() && type.isNotEmpty()) {

            if(type == Constants.EXPENSE) {
                icon = R.drawable.ic_other_expense
            } else {
                icon = R.drawable.ic_other_income
            }

            var category = CategoryEntity(
                name = name,
                type = type,
                icon = icon
            )

            lifecycleScope.launch {
                MainFragment.mCategoryDao.insert(category)
            }

        } else {
            Toast.makeText(applicationContext, "Fill all of the fields!", Toast.LENGTH_LONG).show()
        }

        // Clearing the text fields
        binding?.etName?.text?.clear()
    }
}