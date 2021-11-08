package com.andriybobchuk.myroomdemo

import android.app.Application


/**
 * Application class created only to access the EmployeeDao instance in the main activity
 */
class EmployeeApp:Application() {

    /*
    The point of this lazy is here that it loads the needed value to our variable whenever
    it is NEEDED. Not always, but only when it's needed
     */
    val db by lazy {
        EmployeeDatabase.getInstance(this)
    }
}