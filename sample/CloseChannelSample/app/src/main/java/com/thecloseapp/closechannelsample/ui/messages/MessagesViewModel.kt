package com.thecloseapp.closechannelsample.ui.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MessagesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "There is no channel (yet) loaded in this tab"
    }
    val text: LiveData<String> = _text
}