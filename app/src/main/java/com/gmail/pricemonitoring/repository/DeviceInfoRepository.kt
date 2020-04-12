package com.gmail.pricemonitoring.repository

import androidx.lifecycle.LiveData
import com.gmail.pricemonitoring.dao.DeviceInfoDao
import com.gmail.pricemonitoring.entity.DeviceInfoEntity

class DeviceInfoRepository(private val urlDao: DeviceInfoDao) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allDeviceInfo: LiveData<List<DeviceInfoEntity>> = urlDao.getAllDevices()

    suspend fun insert(deviceInfoEntity: DeviceInfoEntity) {
        urlDao.insert(deviceInfoEntity)
    }

    suspend fun delete(deviceInfoEntity: DeviceInfoEntity) {
        urlDao.delete(deviceInfoEntity)
    }
    suspend fun deleteAll() {
        urlDao.deleteAll()
    }
}