package com.ahmetocak.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.utils.AuthInputChecker
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.domain.firebase.auth.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    var emailValue by mutableStateOf("")
        private set

    var passwordValue by mutableStateOf("")
        private set

    var confirmPasswordValue by mutableStateOf("")
        private set

    fun updateEmailValue(value: String) {
        emailValue = value
    }

    fun updatePasswordValue(value: String) {
        passwordValue = value
    }

    fun updateConfirmPasswordValue(value: String) {
        confirmPasswordValue = value
    }

    fun signUp(onNavigateHome: () -> Unit) {
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

        val isConfirmPasswordOk = AuthInputChecker.checkConfirmPasswordField(
            confirmPassword = confirmPasswordValue,
            password = passwordValue,
            onBlank = { errorMessage ->
                _uiState.update {
                    it.copy(confirmPasswordFieldErrorMessage = errorMessage)
                }
            },
            onUnValid = { errorMessage ->
                _uiState.update {
                    it.copy(confirmPasswordFieldErrorMessage = errorMessage)
                }
            },
            onSuccess = {
                _uiState.update {
                    it.copy(confirmPasswordFieldErrorMessage = null)
                }
            }
        )

        if (isEmailOk && isPasswordOk && isConfirmPasswordOk) {
            _uiState.update {
                it.copy(isLoading = true)
            }
            viewModelScope.launch(Dispatchers.IO) {
                signUpUseCase(
                    email = emailValue,
                    password = passwordValue,
                    onTaskSuccess = onNavigateHome,
                    onTaskFailed = { errorMessage ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessages = listOf(errorMessage)
                            )
                        }
                    }
                )
            }
        }
    }

    fun consumedErrorMessage() {
        _uiState.update {
            it.copy(errorMessages = listOf())
        }
    }
}

data class SignUpUiState(
    val isLoading: Boolean = false,
    val errorMessages: List<UiText> = emptyList(),
    val emailFieldErrorMessage: UiText? = null,
    val passwordFieldErrorMessage: UiText? = null,
    val confirmPasswordFieldErrorMessage: UiText? = null
)