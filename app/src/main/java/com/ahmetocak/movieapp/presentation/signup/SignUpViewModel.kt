package com.ahmetocak.movieapp.presentation.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.helpers.SignUpInputChecker
import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.movieapp.model.firebase.auth.Auth
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
    private val firebaseRepository: FirebaseRepository
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
        val isEmailOk = SignUpInputChecker.checkEmailField(
            email = emailValue,
            onBlank = {
                _uiState.update {
                    it.copy(emailFieldErrorMessage = UiText.StringResource(R.string.blank_field))
                }
            },
            onUnValid = {
                _uiState.update {
                    it.copy(emailFieldErrorMessage = UiText.StringResource(R.string.unvalid_email))
                }
            },
            onSuccess = {
                _uiState.update {
                    it.copy(emailFieldErrorMessage = null)
                }
            }
        )

        val isPasswordOk = SignUpInputChecker.checkPasswordField(
            password = passwordValue,
            onBlank = {
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = UiText.StringResource(R.string.blank_field))
                }
            },
            onUnValid = {
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = UiText.StringResource(R.string.password_length))
                }
            },
            onSuccess = {
                _uiState.update {
                    it.copy(passwordFieldErrorMessage = null)
                }
            }
        )

        val isConfirmPasswordOk = SignUpInputChecker.checkConfirmPasswordField(
            confirmPassword = confirmPasswordValue,
            password = passwordValue,
            onBlank = {
                _uiState.update {
                    it.copy(confirmPasswordFieldErrorMessage = UiText.StringResource(R.string.blank_field))
                }
            },
            onUnValid = {
                _uiState.update {
                    it.copy(confirmPasswordFieldErrorMessage = UiText.StringResource(R.string.passwords_dont_match))
                }
            },
            onSuccess = {
                _uiState.update {
                    it.copy(confirmPasswordFieldErrorMessage = null)
                }
            }
        )

        if (isEmailOk && isPasswordOk && isConfirmPasswordOk) {
            viewModelScope.launch(Dispatchers.IO) {
                _uiState.update {
                    it.copy(isLoading = true)
                }
                firebaseRepository.signUp(auth = Auth(email = emailValue, password = passwordValue))
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onNavigateHome()
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