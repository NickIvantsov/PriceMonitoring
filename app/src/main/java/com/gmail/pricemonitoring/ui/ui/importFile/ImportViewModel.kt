package com.gmail.pricemonitoring.ui.ui.importFile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImportViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Will be available soon!"
    }
    val text: LiveData<String> = _text
}