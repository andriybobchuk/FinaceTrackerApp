package com.andriybobchuk.myroomdemo.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.andriybobchuk.myroomdemo.R
import com.andriybobchuk.myroomdemo.adapters.CategoryItemAdapter
import com.andriybobchuk.myroomdemo.databinding.ActivityCategoryBinding
import com.andriybobchuk.myroomdemo.databinding.DialogCreateCategoryBinding
import com.andriybobchuk.myroomdemo.databinding.DialogUpdateCategoryBinding
import com.andriybobchuk.myroomdemo.fragments.MainFragment
import com.andriybobchuk.myroomdemo.room.CategoryEntity
import com.andriybobchuk.myroomdemo.util.Constants
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {

    private var binding: ActivityCategoryBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding way of inflating the root
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        lifecycleScope.launch {
            MainFragment.mCategoryDao.fetchAllCategories().collect {
                populateCategoryListToUI(ArrayList(it))
            }
        }

        binding?.ivCross?.setOnClickListener {
            finish()
        }

        binding?.fabAdd?.setOnClickListener {
            createCategoryDialog()
        }
    }

    private fun populateCategoryListToUI(categoryList: ArrayList<CategoryEntity>) {

        binding?.rvCategoryList?.layoutManager = GridLayoutManager(this, 2)
        binding?.rvCategoryList?.setHasFixedSize(true)

        // Create an instance of BoardItemsAdapter and pass the boardList to it.
        val adapter = CategoryItemAdapter(categoryList,
            {
                updateId ->
                updateCategoryDialog(updateId)
            }
        )
        binding?.rvCategoryList?.adapter = adapter // Attach the adapter to the recyclerView.
    }

    private fun createCategoryDialog() {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(true)
        val binding = DialogCreateCategoryBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        val accountTypesSpinner = binding?.sType
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.category_types_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            accountTypesSpinner?.adapter = adapter
        }

        binding.btnCreate.setOnClickListener {

            val name = binding?.etName?.text.toString()
            val type = binding?.sType?.selectedItem.toString()
            val icon: Int

            if (name.isNotEmpty() && type.isNotEmpty()) {

                icon = if (type == Constants.EXPENSE) {
                    R.drawable.ic_other_income
                } else {
                    R.drawable.ic_other_expense
                }
                lifecycleScope.launch {
                    MainFragment.mCategoryDao.insert(CategoryEntity(
                        name = name,
                        type = type,
                        icon = icon
                    ))
                }
                updateDialog.dismiss()
            } else {
                Toast.makeText(this, "Fill all of the fields!", Toast.LENGTH_LONG).show()
            }
            // Clearing the text fields
            binding?.etName?.text?.clear()
        }
        //Start the dialog and display it on screen.
        updateDialog.show()
    }

    private fun updateCategoryDialog(id: Int) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(true)
        val binding = DialogUpdateCategoryBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        val accountTypesSpinner = binding?.sType
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.category_types_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            accountTypesSpinner?.adapter = adapter
        }

        //UPDATE FEATURE
        lifecycleScope.launch {
            MainFragment.mCategoryDao.fetchCategoryById(id).collect {
                binding.etName.setText(it?.name) // HAHA, using this I saved the delete feature!
                if(it?.type == Constants.EXPENSE) {
                    binding.sType.setSelection(0)
                } else {
                    binding.sType.setSelection(1)
                }
            }
        }

        binding.btnUpdate.setOnClickListener {

            val name = binding?.etName?.text.toString()
            val type = binding?.sType?.selectedItem.toString()
            val icon: Int

            if (name.isNotEmpty() && type.isNotEmpty()) {

                icon = if (type == Constants.EXPENSE) {
                    R.drawable.ic_other_income
                } else {
                    R.drawable.ic_other_expense
                }
                lifecycleScope.launch {
                    MainFragment.mCategoryDao.update(CategoryEntity(
                        id = id,
                        name = name,
                        type = type,
                        icon = icon
                    ))
                }
                updateDialog.dismiss()
            } else {
                Toast.makeText(this, "Fill all of the fields!", Toast.LENGTH_LONG).show()
            }
            // Clearing the text fields
            binding?.etName?.text?.clear()
        }

        binding.btnDelete.setOnClickListener {
            lifecycleScope.launch {
                MainFragment.mCategoryDao.delete(CategoryEntity(id))
            }
            updateDialog.dismiss()
        }



        //Start the dialog and display it on screen.
        updateDialog.show()
    }




}