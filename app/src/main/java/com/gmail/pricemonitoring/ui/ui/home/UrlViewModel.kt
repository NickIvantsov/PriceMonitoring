package com.gmail.pricemonitoring.ui.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.gmail.pricemonitoring.db.UrlRoomDatabase
import com.gmail.pricemonitoring.entity.UrlEntity
import com.gmail.pricemonitoring.repository.UrlRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UrlViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UrlRepository

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allUrls: LiveData<List<UrlEntity>>

    init {
        val urlssDao = UrlRoomDatabase.getDatabase(application).urlDao()
        repository = UrlRepository(urlssDao)
        allUrls = repository.allUrls
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(urlEntity: UrlEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(urlEntity)
    }

    fun delete(urlEntity: UrlEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(urlEntity)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }
}