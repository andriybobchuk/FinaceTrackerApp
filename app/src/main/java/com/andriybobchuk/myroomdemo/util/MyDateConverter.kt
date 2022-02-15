package com.andriybobchuk.myroomdemo.util

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MyDateConverter(dateString: String) {

    val FORMAT_FROM = SimpleDateFormat("d MMM yyyy")

    // Obtained values:
    val date: Date = FORMAT_FROM.parse(dateString)

    val dayOfTheWeek = DateFormat.format("EEEE", date) as String // Thursday
    val day = DateFormat.format("dd", date) as String // 20
    val monthString = DateFormat.format("MMM", date) as String // Jun
    val monthNumber = DateFormat.format("MM", date) as String // 06
    val year = DateFormat.format("yyyy", date) as String // 2013
}