package com.andriybobchuk.myroomdemo.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColor
import androidx.lifecycle.lifecycleScope
import com.andriybobchuk.myroomdemo.databinding.ActivityUpdateAccountBinding
import com.andriybobchuk.myroomdemo.dialogs.ColorListDialog
import com.andriybobchuk.myroomdemo.fragments.MainFragment
import com.andriybobchuk.myroomdemo.room.AccountEntity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*


class UpdateAccountActivity : AppCompatActivity() {
    private var binding: ActivityUpdateAccountBinding? = null
    private var mSelectedColor: String = "#e2c8b1" // standard color
    private var mCurrency: String = ""
    private var mAccountType: String = ""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding way of inflating the root
        binding = ActivityUpdateAccountBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        populateUiWithOldData()

        binding?.vColor?.setBackgroundColor(Color.parseColor(mSelectedColor))

        binding?.llColor?.setOnClickListener { openColorPickerDialog() }

        binding?.btnAdd?.setOnClickListener {
            updateAccount()
            finish()
        }

        binding?.btnDelete?.setOnClickListener {
            deleteAccount()
            finish()
        }
    }


    private fun populateUiWithOldData() {

        lifecycleScope.launch {

            MainFragment.mAccountDao.fetchAccountById(
                intent.getIntExtra("id", 0)
            ).collect {
                binding?.etName?.setText(it.name)
                mAccountType = it.type
                mCurrency = it.currency
                binding?.etBalance?.setText(it.balance)

                mSelectedColor = it.color
                binding?.vColor?.setBackgroundColor(Color.parseColor(mSelectedColor))
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateAccount() {

        val name = binding?.etName?.text.toString()
        val balance = binding?.etBalance?.text.toString()

        if (
            name.isNotEmpty() &&
            balance.isNotEmpty()
        ) {
            var account = AccountEntity(
                id = intent.getIntExtra("id", 0),
                name = name,
                type = mAccountType,
                currency = mCurrency,
                balance = balance,
                color = mSelectedColor // TODO
            )

            lifecycleScope.launch {
                MainFragment.mAccountDao.update(account)
            }

        } else {
            Toast.makeText(applicationContext, "Fill all of the fields!", Toast.LENGTH_LONG).show()
        }
    }


    private fun deleteAccount() {

        lifecycleScope.launch {
            MainFragment.mAccountDao.delete(AccountEntity(id = intent.getIntExtra("id", 0)))
        }
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
        colorsList.add("#B1E2E2")
        colorsList.add("#D2E2B1")

        colorsList.add("#B3B1E2")
        colorsList.add("#B1E2D0")
        colorsList.add("#E2DDB1")
        colorsList.add("#E2C5B1")

        return colorsList
    }


    /**
     * A function to launch the label color list dialog.
     */
    private fun openColorPickerDialog() {

        val colorsList: ArrayList<String> = colorsList()

        try {
            val listDialog = object : ColorListDialog(
                this@UpdateAccountActivity,
                colorsList,
                "Color Select",
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
