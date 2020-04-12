package com.gmail.pricemonitoring.ui.ui.exportFile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.gmail.pricemonitoring.db.UrlRoomDatabase
import com.gmail.pricemonitoring.entity.DeviceInfoEntity
import com.gmail.pricemonitoring.repository.DeviceInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExportViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DeviceInfoRepository

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allUrls: LiveData<List<DeviceInfoEntity>>

    init {
        val urlssDao = UrlRoomDatabase.getDatabase(application).deviceInfoDao()
        repository = DeviceInfoRepository(urlssDao)
        allUrls = repository.allDeviceInfo
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(deviceInfoEntity: DeviceInfoEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(deviceInfoEntity)
    }

    fun delete(deviceInfoEntity: DeviceInfoEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(deviceInfoEntity)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}