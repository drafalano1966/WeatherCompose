package com.example.weathercompose.viewmodel

import com.example.weathercompose.data.MainDataModel

sealed class UIState {
    data class Success(val data: MainDataModel): UIState()
    data class Failure(val error: String): UIState()
    data class Loading(val isLoading: Boolean = true): UIState()
    object Empty: UIState()
}