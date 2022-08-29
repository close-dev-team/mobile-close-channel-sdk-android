package com.thecloseapp.closechannelsample.ui.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChannelsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Channels are loading"
    }
    val text: LiveData<String> get() = _text

    fun showInfoNoChannels() {
        _text.value = "There are no channels yet, Please register and add a channel in SDK home"
    }
}