package com.ajailani.stateflowlearning.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Entity(tableName = "users")
@Parcelize
data class User(
    @Json(name = "id")
    @PrimaryKey
    val id: Int = 0,
    @Json(name = "name")
    @ColumnInfo(name = "name")
    val name: String = "",
    @Json(name = "email")
    @ColumnInfo(name = "email")
    val email: String = ""
) : Parcelable
