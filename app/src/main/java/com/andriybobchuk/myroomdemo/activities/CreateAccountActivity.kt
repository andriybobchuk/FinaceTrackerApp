package com.andriybobchuk.myroomdemo.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.andriybobchuk.myroomdemo.R
import com.andriybobchuk.myroomdemo.databinding.ActivityCreateAccountBinding
import com.andriybobchuk.myroomdemo.dialogs.ColorListDialog
import com.andriybobchuk.myroomdemo.fragments.MainFragment
import com.andriybobchuk.myroomdemo.room.AccountEntity
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.ArrayList

class CreateAccountActivity : AppCompatActivity() {


    private var binding: ActivityCreateAccountBinding? = null
    private var mSelectedColor: String = "#e2c8b1" // standard color


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding way of inflating the root
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.vColor?.setBackgroundColor(Color.parseColor(mSelectedColor))

        binding?.llColor?.setOnClickListener { openColorPickerDialog() }

        binding?.btnAdd?.setOnClickListener {
            createAccount()
            finish()
        }

        val currenciesSpinner = binding?.sCurrency
        ArrayAdapter.createFromResource(
            applicationContext,
            R.array.currencies_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            currenciesSpinner?.adapter = adapter
        }

        val accountTypesSpinner = binding?.sType
        ArrayAdapter.createFromResource(
            applicationContext,
            R.array.account_types_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            accountTypesSpinner?.adapter = adapter
        }
    }


    private fun createAccount() {

        val name = binding?.etName?.text.toString()
        val currency = binding?.sCurrency?.selectedItem.toString()
        val type = binding?.sType?.selectedItem.toString()
        val balance = binding?.etBalance?.text.toString()
        val color = mSelectedColor

        if (
            name.isNotEmpty() &&
            currency.isNotEmpty() &&
            type.isNotEmpty() &&
            balance.isNotEmpty() &&
            color.isNotEmpty()
        ) {
            var account = AccountEntity(
                name = name,
                currency = currency,
                type = type,
                balance = balance,
                color = color
            )
            //MainFragment().addRecord(MainFragment.mAccountDao, account)

            lifecycleScope.launch {
                MainFragment.mAccountDao.insert(account)
            }

        } else {
            Toast.makeText(applicationContext, "Fill all of the fields!", Toast.LENGTH_LONG).show()
        }

        // Clearing the text fields
        binding?.etName?.text?.clear()
        binding?.etBalance?.text?.clear()
    }


    /**
     * A function to add some static label colors to the list.
     */
    private fun colorsList(): ArrayList<String> {

        val colorsList: ArrayList<String> = ArrayList()
        colorsList.add("#e2c8b1")
        colorsList.add("#B2CFEA")
        colorsList.add("#C9B1E2")
        colorsList.add("#B1E2B3")
        colorsList.add("#E2B1D2")
        colorsList.add("#E2B1B1")

        return colorsList
    }


    /**
     * A function to launch the label color list dialog.
     */
    private fun openColorPickerDialog() {

        val colorsList: ArrayList<String> = colorsList()

        try {
            val listDialog = object: ColorListDialog(
                this@CreateAccountActivity,
                colorsList,
                "Select the color",
                mSelectedColor
            ) {
                override fun onItemSelected(color: String) {
                    mSelectedColor = color
                    binding?.vColor?.setBackgroundColor(Color.parseColor(mSelectedColor))
                }
            }
            listDialog.show()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "${e}", Toast.LENGTH_LONG).show()
        }

    }
}