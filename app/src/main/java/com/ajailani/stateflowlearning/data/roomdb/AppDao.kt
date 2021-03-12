package com.ajailani.stateflowlearning.data.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ajailani.stateflowlearning.data.model.User

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUsers(usersList: List<User>)

    @Query("SELECT * FROM users")
    suspend fun getUsers(): List<User>?
}