package com.andriybobchuk.myroomdemo.util

import android.util.Log
import android.widget.Toast

class HelperFunctions {

    fun funcThatMightThrow(): Boolean {

        val TAG = "D-BUG";

        try {
            //  throwing function here:

        } catch (e: Exception) {
            Log.e(TAG, "${e.printStackTrace()}")
            return false
        }
        return true
    }

}