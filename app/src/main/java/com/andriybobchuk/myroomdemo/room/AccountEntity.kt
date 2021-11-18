package com.andriybobchuk.myroomdemo.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ENTITY is one of three ROOM components.
 * Here you only declare your model.
 */
@Entity(tableName = "account-table")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val type: String = "",
    val currency: String = "",
    val balance: String = ""
)