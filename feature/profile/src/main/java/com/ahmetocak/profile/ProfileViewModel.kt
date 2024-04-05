package com.ahmetocak.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.common.helpers.DialogUiEvent
import com.ahmetocak.common.helpers.UiText
import com.ahmetocak.common.helpers.handleTaskError
import com.ahmetocak.domain.firebase.auth.DeleteAccountUseCase
import com.ahmetocak.domain.firebase.auth.GetUserEmailUseCase
import com.ahmetocak.domain.firebase.auth.SignOutUseCase
import com.ahmetocak.domain.firebase.storage.GetUserProfileImageUseCase
import com.ahmetocak.domain.firebase.storage.UploadProfileImageUseCase
import com.ahmetocak.domain.preferences.GetAppThemeUseCase
import com.ahmetocak.domain.preferences.GetDynamicColorUseCase
import com.ahmetocak.domain.preferences.UpdateAppThemeUseCase
import com.ahmetocak.domain.preferences.UpdateDynamicColorUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val uploadProfileImageUseCase: UploadProfileImageUseCase,
    private val getProfileImageUseCase: GetUserProfileImageUseCase,
    private val getAppThemeUseCase: GetAppThemeUseCase,
    private val updateAppThemeUseCase: UpdateAppThemeUseCase,
    private val getDynamicColorUseCase: GetDynamicColorUseCase,
    private val updateDynamicColorUseCase: UpdateDynamicColorUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getUserEmailUseCase: GetUserEmailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        getTheme()
        getDynamicColor()
        getUserProfileImage()
        _uiState.update {
            it.copy(userEmail = getUserEmailUseCase() ?: "")
        }
    }

    var password by mutableStateOf("")
        private set

    fun updatePasswordValue(value: String) {
        password = value
    }

    fun startDeleteAccountDialog() {
        _uiState.update {
            it.copy(deleteAccountDialogUiEvent = DialogUiEvent.Active)
        }
    }

    fun endDeleteAccountDialog() {
        _uiState.update {
            it.copy(deleteAccountDialogUiEvent = DialogUiEvent.InActive)
        }
    }

    fun deleteUserAccount(onAccountDeleteEnd: () -> Unit) {
        _uiState.update {
            it.copy(deleteAccountDialogUiEvent = DialogUiEvent.Loading)
        }
        viewModelScope.launch(ioDispatcher) {
            deleteAccountUseCase(
                email = _uiState.value.userEmail,
                password = password,
                onSuccess = {
                    _uiState.update {
                        it.copy(deleteAccountDialogUiEvent = DialogUiEvent.InActive)
                    }
                    onAccountDeleteEnd()
                },
                onFailed = { errorMessage ->
                    _uiState.update {
                        it.copy(userMessages = listOf(errorMessage))
                    }
                },
                scope = viewModelScope
            )
        }
    }

    fun consumedUserMessage() {
        _uiState.update {
            it.copy(userMessages = emptyList())
        }
    }

    fun uploadUserProfileImage(imageUri: Uri) {
        _uiState.update {
            it.copy(isProfileImageUploading = true)
        }
        viewModelScope.launch(ioDispatcher) {
            uploadProfileImageUseCase(imageUri).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update {
                        it.copy(profileImgUri = imageUri, isProfileImageUploading = false)
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            userMessages = listOf(handleTaskError(e = task.exception)),
                            isProfileImageUploading = false
                        )
                    }
                }
            }
        }
    }

    private fun getUserProfileImage() {
        viewModelScope.launch(ioDispatcher) {
            getProfileImageUseCase().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update {
                        it.copy(profileImgUri = task.result)
                    }
                }
            }
        }
    }

    fun setTheme(darkTheme: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            updateAppThemeUseCase(darkTheme)
        }
    }

    private fun getTheme() {
        viewModelScope.launch(ioDispatcher) {
            getAppThemeUseCase().collect { darkMode ->
                _uiState.update {
                    it.copy(isDarkModeOn = darkMode)
                }
            }
        }
    }

    fun setDynamicColor(dynamicColor: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            updateDynamicColorUseCase(dynamicColor)
        }
    }

    private fun getDynamicColor() {
        viewModelScope.launch(ioDispatcher) {
            getDynamicColorUseCase().collect { dynamicColor ->
                _uiState.update {
                    it.copy(isDynamicColorOn = dynamicColor)
                }
            }
        }
    }

    fun handleOnLogOutClick() {
        viewModelScope.launch(ioDispatcher) {
            signOutUseCase(onFailed = { errorMessage ->
                _uiState.update {
                    it.copy(userMessages = listOf(errorMessage))
                }
            })
        }
    }
}

data class ProfileUiState(
    val profileImgUri: Uri? = null,
    val userEmail: String = "",
    val userMessages: List<UiText> = emptyList(),
    val deleteAccountDialogUiEvent: DialogUiEvent = DialogUiEvent.InActive,
    val isProfileImageUploading: Boolean = false,
    val isDarkModeOn: Boolean = false,
    val isDynamicColorOn: Boolean = false
)