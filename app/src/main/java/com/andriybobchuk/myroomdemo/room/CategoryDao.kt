package com.andriybobchuk.myroomdemo.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) interface is the second of three components of ROOM.
 * Here you just declare all the CRUD (Create-Read-Update-Delete) operations
 */
@Dao
interface CategoryDao {

    /*
    We are making it a suspend function using a coroutine class as this operation
    takes relatively long time. That means we want to run it on background thread
     */
    @Insert
    suspend fun insert(categoryEntity: AccountEntity)

    @Update
    suspend fun update(categoryEntity: AccountEntity)

    @Delete
    suspend fun delete(categoryEntity: AccountEntity)

    @Query("SELECT * FROM `category-table`")
    fun fetchAllCategories():Flow<List<CategoryEntity>>

    @Query("SELECT * FROM `account-table` WHERE id=:id")
    fun fetchAccountById(id: Int):Flow<CategoryEntity>

    @Query("SELECT * FROM `account-table` WHERE name=:name")
    fun fetchAccountByName(name: String):Flow<AccountEntity>
}