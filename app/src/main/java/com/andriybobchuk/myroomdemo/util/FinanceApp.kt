package com.andriybobchuk.myroomdemo.util

import android.app.Application
import com.andriybobchuk.myroomdemo.room.AppDatabase


/**
 * Application class created only to access the EmployeeDao instance in the main activity
 */
class FinanceApp:Application() {

    /*
    The point of this lazy is here that it loads the needed value to our variable whenever
    it is NEEDED. Not always, but only when it's needed
     */
    val db by lazy {
        AppDatabase.getInstance(this)
    }
}