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
    suspend fun insert(categoryEntity: CategoryEntity)

    @Update
    suspend fun update(categoryEntity: CategoryEntity)

    @Delete
    suspend fun delete(categoryEntity: CategoryEntity)

    @Query("SELECT * FROM `category`")
    fun fetchAllCategories():Flow<List<CategoryEntity>>

    @Query("SELECT * FROM `category` WHERE id=:id")
    fun fetchCategoryById(id: Int):Flow<CategoryEntity>

    @Query("SELECT * FROM `category` WHERE name=:name")
    fun fetchCategoryByName(name: String):Flow<CategoryEntity>
}