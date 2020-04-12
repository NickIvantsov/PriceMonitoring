package com.gmail.pricemonitoring.repository

import androidx.lifecycle.LiveData
import com.gmail.pricemonitoring.dao.UrlDao
import com.gmail.pricemonitoring.entity.UrlEntity

class UrlRepository(private val urlDao: UrlDao) {
    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allUrls: LiveData<List<UrlEntity>> = urlDao.getAlphabetizedWords()

    suspend fun insert(urlEntity: UrlEntity) {
        urlDao.insert(urlEntity)
    }

    suspend fun delete(urlEntity: UrlEntity) {
        urlDao.delete(urlEntity)
    }
    suspend fun deleteAll() {
        urlDao.deleteAll()
    }
}