package com.ahmetocak.movieapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.data.repository.datastore.DataStoreRepository
import com.ahmetocak.movieapp.common.helpers.connectivity.ConnectivityObserver
import com.ahmetocak.movieapp.common.helpers.connectivity.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
        initializeTheme()
        observeTheme()
        observeDynamicColor()
        checkIsDeviceOnline()
    }

    private fun initializeTheme() {
        runBlocking(ioDispatcher) {
            _uiState.update {
                it.copy(
                    isDarkModeOn = dataStoreRepository.getAppTheme().first(),
                    isDynamicColorOn = dataStoreRepository.getDynamicColor().first()
                )
            }
        }
    }

    private fun observeTheme() {
        viewModelScope.launch(ioDispatcher) {
            dataStoreRepository.getAppTheme().collect { isDarkMode ->
                _uiState.update {
                    it.copy(isDarkModeOn = isDarkMode)
                }
            }
        }
    }

    private fun observeDynamicColor() {
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