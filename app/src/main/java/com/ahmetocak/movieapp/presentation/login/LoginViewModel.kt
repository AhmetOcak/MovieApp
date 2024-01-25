package com.ahmetocak.movieapp.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.DialogUiEvent
import com.ahmetocak.movieapp.common.helpers.LoginInputChecker
import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.common.helpers.isValidEmail
import com.ahmetocak.movieapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.movieapp.model.auth.Auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
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
        val isEmailOk = LoginInputChecker.checkEmailField(
            email = emailValue,
            onBlank = {
                _uiState.update {
                    it.copy(emailFieldErrorMessage = UiText.StringResource(R.string.blank_field))
                }
            },
            onSuccess = {
                _uiState.update {
                    it.copy(emailFieldErrorMessage = null)
                }
            }
        )

        val isPasswordOk = LoginInputChecker.checkPasswordField(
            password = passwordValue,
            onBlank = {
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = UiText.StringResource(R.string.blank_field))
                }
            },
            onSuccess = {
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = null)
                }
            }
        )

        if (isEmailOk && isPasswordOk) {
            viewModelScope.launch(Dispatchers.IO) {
                _uiState.update {
                    it.copy(isLoading = true)
                }
                firebaseRepository.login(auth = Auth(emailValue, passwordValue))
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onSuccess()
                        } else {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessages = listOf(
                                        task.exception?.message?.let { message ->
                                            UiText.DynamicString(message)
                                        } ?: kotlin.run {
                                            UiText.StringResource(R.string.unknown_error)
                                        }
                                    )
                                )
                            }
                        }
                    }
            }
        }
    }

    fun sendPasswordResetMail() {
        if (passwordResetEmailValue.isValidEmail()) {
            viewModelScope.launch(Dispatchers.IO) {
                _uiState.update {
                    it.copy(dialogUiEvent = DialogUiEvent.Loading)
                }
                firebaseRepository.sendResetPasswordEmail(passwordResetEmailValue)
                    .addOnCompleteListener { task ->
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
                                    errorMessages = listOf(
                                        task.exception?.message?.let { message ->
                                            UiText.DynamicString(message)
                                        } ?: kotlin.run {
                                            UiText.StringResource(R.string.unknown_error)
                                        }
                                    )
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