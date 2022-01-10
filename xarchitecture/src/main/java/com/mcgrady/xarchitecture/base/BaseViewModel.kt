package com.mcgrady.xarchitecture.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

/**
 * Created by mcgrady on 2022/1/7.
 */
abstract class BaseViewModel : ViewModel() {

    fun <T> Flow<T>.asLiveDataOnViewModelScope(): LiveData<T> {
        return asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)
    }
}