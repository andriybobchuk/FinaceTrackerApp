package com.andriybobchuk.myroomdemo.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transactionEntity: TransactionEntity)

    @Update
    suspend fun update(transactionEntity: TransactionEntity)

    @Delete
    suspend fun delete(transactionEntity: TransactionEntity)

    @Query("SELECT * FROM `transaction-table`")
    fun fetchAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM `transaction-table` WHERE id=:id")
    fun fetchTransactionsById(id: Int): Flow<TransactionEntity>
}