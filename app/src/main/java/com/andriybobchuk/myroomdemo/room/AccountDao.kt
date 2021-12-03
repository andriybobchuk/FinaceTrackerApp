package com.andriybobchuk.myroomdemo.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) interface is the second of three components of ROOM.
 * Here you just declare all the CRUD (Create-Read-Update-Delete) operations
 */
@Dao
interface AccountDao {

    /*
    We are making it a suspend function using a coroutine class as this operation
    takes relatively long time. That means we want to run it on background thread
     */
    @Insert
    suspend fun insert(accountEntity: AccountEntity)

    @Update
    suspend fun update(accountEntity: AccountEntity)

    @Delete
    suspend fun delete(accountEntity: AccountEntity)

    @Query("SELECT * FROM `account-table`")
    fun fetchAllAccounts():Flow<List<AccountEntity>>

    @Query("SELECT * FROM `account-table` WHERE id=:id")
    fun fetchAccountById(id: Int):Flow<AccountEntity>

    @Query("SELECT * FROM `account-table` WHERE name=:name")
    fun fetchAccountByName(name: String):Flow<AccountEntity>
}