package com.gmail.pricemonitoring.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "url_table")
data class UrlEntity(
    @PrimaryKey
    val url: String,
    val time: String = Calendar.getInstance().time.toString(),
    @ColumnInfo(name = "time_milliseconds")
    val timeMilliseconds: Long = Calendar.getInstance().timeInMillis
)