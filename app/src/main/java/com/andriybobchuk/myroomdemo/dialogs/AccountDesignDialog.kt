package com.andriybobchuk.myroomdemo.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.andriybobchuk.myroomdemo.R
import com.andriybobchuk.myroomdemo.databinding.DesignNewAccountDialogFragmentBinding
import com.andriybobchuk.myroomdemo.fragments.MainFragment
import com.andriybobchuk.myroomdemo.room.AccountDao
import com.andriybobchuk.myroomdemo.room.AccountEntity
import java.util.ArrayList

class AccountDesignDialog(
    val mainActivityContext: Context,
    var accountDao: AccountDao
) : Dialog(mainActivityContext) {

    // A global variable for selected label color
    private var mSelectedColor: String = ""

    init {
        setCancelable(true)
    }

    var binding: DesignNewAccountDialogFragmentBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //setContentView(R.layout.design_new_account_dialog_fragment)
        binding = DesignNewAccountDialogFragmentBinding.inflate(
            LayoutInflater.from(context)
        )
        setContentView(binding!!.root)

        window?.setLayout(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )

//            mSelectedColor =
//            if (mSelectedColor.isNotEmpty()) {
//                setColor()
//            }

        findViewById<TextView>(R.id.tv_select_color).setOnClickListener {
            labelColorsListDialog()
        }

        //val accountDao: AccountDao? = null
        //var accountDao = (mainActivityContext as FinanceApp).db.accountDao()
        binding?.btnAdd?.setOnClickListener {



            val name = binding?.etName?.text.toString()
            val currency = binding?.sCurrency?.selectedItem.toString()
            val type = binding?.sType?.selectedItem.toString()
            val balance = binding?.etBalance?.text.toString()
            val color = binding?.tvSelectColor?.text.toString()

            if (name.isNotEmpty() && currency.isNotEmpty() && type.isNotEmpty() && balance.isNotEmpty() && color.isNotEmpty()) {
                var account = AccountEntity(
                    name = name,
                    currency = currency,
                    type = type,
                    balance = balance,
                    color = color
                )
                //MainFragment().addRecord(accountDao, account)
                dismiss()

            } else {
                Toast.makeText(context, "Fill all of the fields!", Toast.LENGTH_LONG).show()
            }

            // Clearing the text fields
            binding?.etName?.text?.clear()
            binding?.etBalance?.text?.clear()
        }


        val currenciesSpinner = binding?.sCurrency
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            context,
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
            context,
            R.array.account_types_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            accountTypesSpinner?.adapter = adapter
        }
    }

    /**
     * A function to remove the text and set the label color to the TextView.
     */
    private fun setColor() {
        findViewById<TextView>(R.id.tv_select_color).text = mSelectedColor
        //findViewById<TextView>(R.id.tv_select_color).visibility = View.GONE
        findViewById<TextView>(R.id.tv_select_color).setBackgroundColor(Color.parseColor(mSelectedColor))
    }

    /**
     * A function to add some static label colors in the list.
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
    private fun labelColorsListDialog() {

        val colorsList: ArrayList<String> = colorsList()

        val listDialog = object: ColorListDialog(
            context,
            colorsList,
            "str_select_label_color",
            mSelectedColor
        ) {
            override fun onItemSelected(color: String) {
                mSelectedColor = color
                setColor()
            }
        }
        listDialog.show()
    }
}