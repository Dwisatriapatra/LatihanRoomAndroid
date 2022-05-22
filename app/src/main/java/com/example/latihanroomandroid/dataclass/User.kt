package com.example.latihanroomandroid.dataclass

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class User(
    @PrimaryKey(autoGenerate = true) val ud : Int?,
    @ColumnInfo(name = "username") val username : String?,
    @ColumnInfo(name = "name") val name : String?,
    @ColumnInfo(name = "password") val password : String?
) : Parcelable