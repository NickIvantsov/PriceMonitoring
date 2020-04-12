package com.gmail.pricemonitoring.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gmail.pricemonitoring.entity.UrlEntity

@Dao
interface UrlDao {
    @Query("SELECT * from url_table ORDER BY time_milliseconds DESC")
    fun getAlphabetizedWords(): LiveData<List<UrlEntity>>

    @Delete
    suspend fun delete(urlEntity: UrlEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(urlEntity: UrlEntity)

    @Query("DELETE FROM url_table")
    suspend fun deleteAll()
}