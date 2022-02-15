package com.andriybobchuk.myroomdemo.models

data class ExpenseModel(
    val id: Int = 0,
    val date: String = "",
    val amount: String = "",
    val category: String = "",
    val account: String = "",
    val currency: String = "",
    val description: String = ""
)