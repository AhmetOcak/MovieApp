package com.ahmetocak.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.utils.AuthInputChecker
import com.ahmetocak.common.helpers.DialogUiEvent
import com.ahmetocak.common.helpers.Response
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.common.helpers.handleTaskError
import com.ahmetocak.common.isValidEmail
import com.ahmetocak.domain.firebase.auth.LoginUseCase
import com.ahmetocak.domain.firebase.auth.SendResetPasswordEmailUseCase
import com.ahmetocak.domain.firebase.firestore.GetMovieDataUseCase
import com.ahmetocak.domain.movie.AddMovieToDbWatchListUseCase
import com.ahmetocak.model.firebase.Auth
import com.ahmetocak.model.firebase.WatchList
import com.ahmetocak.model.firebase.WatchListMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val loginUseCase: LoginUseCase,
    private val sendResetPasswordEmailUseCase: SendResetPasswordEmailUseCase,
    private val getMovieDataUseCase: GetMovieDataUseCase,
    private val addMovieToWatchListUseCase: AddMovieToDbWatchListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    var emailValue by mutableStateOf("")
        private set

    var passwordValue by mutableStateOf("")
        private set

    var passwordResetEmailValue by mutableStateOf("")
        private set

    fun updateEmailValue(value: String) {
        emailValue = value
    }

    fun updatePasswordValue(value: String) {
        passwordValue = value
    }

    fun updatePasswordResetMail(value: String) {
        passwordResetEmailValue = value
    }

    fun startResetPasswordDialog() {
        _uiState.update {
            it.copy(dialogUiEvent = DialogUiEvent.Active)
        }
    }

    fun endResetPasswordDialog() {
        _uiState.update {
            it.copy(dialogUiEvent = DialogUiEvent.InActive)
        }
    }

    fun login(onSuccess: () -> Unit) {
        val isEmailOk = AuthInputChecker.checkEmailField(
            email = emailValue,
            onBlank = { errorMessage ->
                _uiState.update {
                    it.copy(emailFieldErrorMessage = errorMessage)
                }
            },
            onUnValid = { errorMessage ->
                _uiState.update {
                    it.copy(emailFieldErrorMessage = errorMessage)
                }
            },
            onSuccess = {
                _uiState.update {
                    it.copy(emailFieldErrorMessage = null)
                }
            }
        )

        val isPasswordOk = AuthInputChecker.checkPasswordField(
            password = passwordValue,
            onBlank = { errorMessage ->
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = errorMessage)
                }
            },
            onUnValid = { errorMessage ->
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = errorMessage)
                }
            },
            onSuccess = {
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = null)
                }
            }
        )

        if (isEmailOk && isPasswordOk) {
            viewModelScope.launch(ioDispatcher) {
                _uiState.update {
                    it.copy(isLoading = true)
                }
                loginUseCase(auth = Auth(emailValue, passwordValue)).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        saveUserWatchListDataToLocalDatabase(onSuccess)
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessages = listOf(handleTaskError(e = task.exception))
                            )
                        }
                    }
                }
            }
        }
    }

    fun sendPasswordResetMail() {
        if (passwordResetEmailValue.isValidEmail()) {
            viewModelScope.launch(ioDispatcher) {
                _uiState.update {
                    it.copy(dialogUiEvent = DialogUiEvent.Loading)
                }
                sendResetPasswordEmailUseCase(email = passwordResetEmailValue).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.update {
                            it.copy(
                                dialogUiEvent = DialogUiEvent.InActive,
                                userMessages = listOf(UiText.StringResource(R.string.password_reset_mail_sent))
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                errorMessages = listOf(handleTaskError(e = task.exception))
                            )
                        }
                    }
                }
            }
        } else {
            _uiState.update {
                it.copy(passwordResetFieldErrorMessage = UiText.StringResource(R.string.unvalid_email))
            }
        }
    }

    private fun saveUserWatchListDataToLocalDatabase(onSuccess: () -> Unit) {
        viewModelScope.launch(ioDispatcher) {

            getMovieDataUseCase().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document.exists()) {
                        val watchList = document.toObject(WatchList::class.java)?.watchList
                        watchList?.forEach {
                            addMovieToWatchList(it)
                        }
                    }
                    onSuccess()
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessages = listOf(
                                handleTaskError(
                                    e = task.exception,
                                    defaultErrorMessage = UiText.StringResource(R.string.watch_list_get_error)
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    private fun addMovieToWatchList(watchListMovie: WatchListMovie) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = addMovieToWatchListUseCase(watchListMovie)) {
                is Response.Success -> {}

                is Response.Error -> {
                    _uiState.update {
                        it.copy(errorMessages = listOf(response.errorMessage))
                    }
                }
            }
        }
    }

    fun consumedErrorMessage() {
        _uiState.update {
            it.copy(errorMessages = emptyList())
        }
    }

    fun consumedUserMessage() {
        _uiState.update {
            it.copy(userMessages = emptyList())
        }
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessages: List<UiText> = emptyList(),
    val userMessages: List<UiText> = emptyList(),
    val emailFieldErrorMessage: UiText? = null,
    val passwordFieldErrorMessage: UiText? = null,
    val passwordResetFieldErrorMessage: UiText? = null,
    val dialogUiEvent: DialogUiEvent = DialogUiEvent.InActive
)