package com.andriybobchuk.myroomdemo.util

import android.content.Context
import android.graphics.Color
import com.andriybobchuk.myroomdemo.R
import java.io.File

object Constants {

    // For the Category type spinner
    const val INCOME: String = "Income"
    const val EXPENSE: String = "Expense"

    const val DOCUMENT_AUTHOR: String = "Ins'n'Outs"
    const val DOCUMENT_CREATOR: String = "Ins'n'Outs"


    fun getAppPath(context: Context): String {
        val directory = File(context.getExternalFilesDir(null).toString()
        + File.separator
        + context.resources.getString(R.string.app_name)
        + File.separator)
        if(!directory.exists()) { directory.mkdir() }
        return directory.path + File.separator
    }

}