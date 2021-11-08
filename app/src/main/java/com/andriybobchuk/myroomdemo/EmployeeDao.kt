package com.andriybobchuk.myroomdemo

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) interface is the second of three components of ROOM.
 * Here you just declare all the CRUD (Create-Read-Update-Delete) operations
 */
@Dao
interface EmployeeDao {

    /*
    We are making it a suspend function using a coroutine class as this operation
    takes relatively long time. That means we want to run it on background thread
     */
    @Insert
    suspend fun insert(employeeEntity: EmployeeEntity)

    @Update
    suspend fun update(employeeEntity: EmployeeEntity)

    @Delete
    suspend fun delete(employeeEntity: EmployeeEntity)

    @Query("SELECT * FROM `employee-table`")
    fun fetchAllEmployees():Flow<List<EmployeeEntity>>

    @Query("SELECT * FROM `employee-table` WHERE id=:id")
    fun fetchEmployeeById(id: Int):Flow<EmployeeEntity>
}