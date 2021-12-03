package com.andriybobchuk.myroomdemo.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/**
 * DATABASE is the last component of ROOM.
 * Here you only create a storage (DATABASE) for all of your tables (ENTITIES).
 *
 * + Based on a SINGLETON pattern you create a table only if it wasn't created before
 */
@Database(entities = [AccountEntity::class, TransactionEntity::class], version = 4)
abstract class AppDatabase:RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao


    /*
    SINGLETON Pattern
    Allows to avoid creating many instances of a database as it is expensive.
    Singleton pattern can be done in two ways:
        1) Deleting the previous instance every time you create a new one;
        2) Giving you an old instance
     */
    companion object {

        // keeps an instance of existing db not to create several of them
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Multiple threads can ask for the same database at a time
        // To ensure we only instantiate it once we use a synchronized block inside
        fun getInstance(context: Context): AppDatabase {

            // Only one thread may enter a synchronized block at a time
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "employee_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return  instance
            }
        }
    }
}