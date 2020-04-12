package com.gmail.pricemonitoring.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "device_info_table")
@Parcelize
data class DeviceInfoEntity(
    @PrimaryKey
    val id: String,
    val device: String,
    val price: String
) : Parcelable