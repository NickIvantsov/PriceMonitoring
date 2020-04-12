package com.gmail.pricemonitoring.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gmail.pricemonitoring.entity.DeviceInfoEntity

@Dao
interface DeviceInfoDao {
    @Query("SELECT * from device_info_table")
    fun getAllDevices(): LiveData<List<DeviceInfoEntity>>

    @Delete
    suspend fun delete(deviceInfoEntity: DeviceInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deviceInfoEntity: DeviceInfoEntity)

    @Query("DELETE FROM device_info_table")
    suspend fun deleteAll()
}