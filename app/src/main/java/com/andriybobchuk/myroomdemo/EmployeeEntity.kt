package com.andriybobchuk.myroomdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * ENTITY is one of three ROOM components.
 * Here you only declare your model.
 */
@Entity(tableName = "employee-table")
data class EmployeeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",

    // If you want to change an internal(database) name of some property(column)
    // just add the @ColumnInfo annotation
    @ColumnInfo(name = "email-id")
    val email: String = ""
)