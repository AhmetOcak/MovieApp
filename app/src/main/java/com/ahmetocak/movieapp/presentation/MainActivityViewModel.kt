package com.ahmetocak.movieapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.data.repository.datastore.DataStoreRepository
import com.ahmetocak.movieapp.utils.ConnectivityObserver
import com.ahmetocak.movieapp.utils.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        getTheme()
        getDynamicColor()
        checkIsDeviceOnline()
    }

    private fun getTheme() {
        viewModelScope.launch(ioDispatcher) {
            dataStoreRepository.getAppTheme().collect { darkMode ->
                _uiState.update {
                    it.copy(isDarkModeOn = darkMode)
                }
            }
        }
    }

    private fun getDynamicColor() {
        viewModelScope.launch(ioDispatcher) {
            dataStoreRepository.getDynamicColor().collect { dynamicColor ->
                _uiState.update {
                    it.copy(isDynamicColorOn = dynamicColor)
                }
            }
        }
    }

    private fun checkIsDeviceOnline() {
        viewModelScope.launch(ioDispatcher) {
            networkConnectivityObserver.observer().collect { networkStatus ->
                when (networkStatus) {
                    ConnectivityObserver.Status.Available -> {}

                    ConnectivityObserver.Status.Unavailable -> {
                        _uiState.update {
                            it.copy(userMessages = listOf(UiText.StringResource(R.string.internet_error)))
                        }
                    }

                    ConnectivityObserver.Status.Lost -> {
                        _uiState.update {
                            it.copy(userMessages = listOf(UiText.StringResource(R.string.internet_lost_error)))
                        }
                    }
                }
            }
        }
    }

    fun consumedUserMessage() {
        _uiState.update {
            it.copy(userMessages = emptyList())
        }
    }
}

data class MainUiState(
    val isDarkModeOn: Boolean = true,
    val isDynamicColorOn: Boolean = false,
    val userMessages: List<UiText> = emptyList()
)