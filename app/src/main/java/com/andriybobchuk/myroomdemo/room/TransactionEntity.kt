package com.andriybobchuk.myroomdemo.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andriybobchuk.myroomdemo.R

@Entity(tableName = "transaction-table")
data class TransactionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String = "",
    val amount: String = "",
    val category: String = "",
    val categoryType: String = "",
    val account: String = "",
    val currency: String = "",
    val description: String = ""
)