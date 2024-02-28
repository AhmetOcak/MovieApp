package com.ahmetocak.movieapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.domain.preferences.GetAppThemeUseCase
import com.ahmetocak.domain.preferences.GetDynamicColorUseCase
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.connectivity.ConnectivityObserver
import com.ahmetocak.movieapp.connectivity.NetworkConnectivityObserver
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
    private val ioDispatcher: CoroutineDispatcher,
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val getAppThemeUseCase: GetAppThemeUseCase,
    private val getDynamicColorUseCase: GetDynamicColorUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        initializeTheme()
        observeTheme()
        observeDynamicColor()
        observeNetworkConnectivity()
    }

    private fun initializeTheme() {
        runBlocking(ioDispatcher) {
            _uiState.update {
                it.copy(
                    isDarkModeOn = getAppThemeUseCase().first(),
                    isDynamicColorOn = getDynamicColorUseCase().first()
                )
            }
        }
    }

    private fun observeTheme() {
        viewModelScope.launch(ioDispatcher) {
            getAppThemeUseCase().collect { isDarkMode ->
                _uiState.update {
                    it.copy(isDarkModeOn = isDarkMode)
                }
            }
        }
    }

    private fun observeDynamicColor() {
        viewModelScope.launch(ioDispatcher) {
            getDynamicColorUseCase().collect { isDynamicColorOn ->
                _uiState.update {
                    it.copy(isDynamicColorOn = isDynamicColorOn)
                }
            }
        }
    }

    private fun observeNetworkConnectivity() {
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