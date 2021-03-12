package com.ajailani.stateflowlearning.data.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ajailani.stateflowlearning.data.model.User

@Database(entities = [User::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        private const val DB_NAME = "STATEFLOWLEARNING_DB"
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            synchronized(AppDatabase::class) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()
            }

            return instance
        }
    }
}