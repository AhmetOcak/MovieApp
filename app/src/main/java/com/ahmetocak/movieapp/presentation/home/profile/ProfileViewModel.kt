package com.ahmetocak.movieapp.presentation.home.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.DialogUiEvent
import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.data.repository.datastore.DataStoreRepository
import com.ahmetocak.movieapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.movieapp.data.repository.movie.MovieRepository
import com.ahmetocak.movieapp.model.firebase.auth.Auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageException
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
    private val firebaseRepository: FirebaseRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val movieRepository: MovieRepository,
    private val firebaseAuth: FirebaseAuth,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        getTheme()
        getDynamicColor()
        getUserProfileImage()
        _uiState.update {
            it.copy(userEmail = firebaseAuth.currentUser?.email ?: "")
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
            reAuthenticate {
                deleteMovieDocument {
                    deleteUserProfileImage {
                        clearMovieLocalDatabase {
                            deleteAccount {
                                _uiState.update {
                                    it.copy(deleteAccountDialogUiEvent = DialogUiEvent.InActive)
                                }
                                onAccountDeleteEnd()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun reAuthenticate(onSuccess: () -> Unit) {
        firebaseRepository.reAuthenticate(
            auth = Auth(
                email = _uiState.value.userEmail,
                password = password
            )
        )?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                handleDeleteAccountError(task.exception)
            }
        }
    }

    private fun deleteAccount(onSuccess: () -> Unit) {
        firebaseRepository.deleteAccount()?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                handleDeleteAccountError(task.exception)
            }
        }
    }

    private fun deleteMovieDocument(onSuccess: () -> Unit) {
        firebaseRepository.deleteMovieDocument().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                handleDeleteAccountError(task.exception)
            }
        }
    }

    private fun deleteUserProfileImage(onSuccess: () -> Unit) {
        firebaseRepository.deleteUserProfileImage().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else if (task.exception is StorageException) {
                if ((task.exception as StorageException).errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {
                    onSuccess()
                }
            } else {
                handleDeleteAccountError(task.exception)
            }
        }
    }

    private fun clearMovieLocalDatabase(onSuccess: () -> Unit = {}) {
        viewModelScope.launch(ioDispatcher) {
            when (val response = movieRepository.deleteWatchList()) {
                is Response.Success -> {
                    onSuccess()
                }

                is Response.Error -> {
                    _uiState.update {
                        it.copy(userMessages = listOf(response.errorMessage))
                    }
                }
            }
        }
    }

    private fun handleDeleteAccountError(exception: Exception?) {
        _uiState.update {
            it.copy(
                userMessages = listOf(
                    exception?.message?.let { message ->
                        UiText.DynamicString(message)
                    } ?: UiText.StringResource(R.string.unknown_error)),
                deleteAccountDialogUiEvent = DialogUiEvent.Active
            )
        }
    }

    fun userMessageConsumed() {
        _uiState.update {
            it.copy(userMessages = emptyList())
        }
    }

    fun uploadUserProfileImage(imageUri: Uri?) {
        _uiState.update {
            it.copy(isProfileImageUploading = true)
        }
        viewModelScope.launch(ioDispatcher) {
            imageUri?.let { uri ->
                firebaseRepository.uploadProfileImage(uri).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _uiState.update {
                            it.copy(profileImgUri = uri, isProfileImageUploading = false)
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                userMessages = listOf(task.exception?.message?.let { message ->
                                    UiText.DynamicString(message)
                                } ?: kotlin.run { UiText.StringResource(R.string.unknown_error) }),
                                isProfileImageUploading = false
                            )
                        }
                    }
                }
            } ?: kotlin.run {
                _uiState.update {
                    it.copy(
                        userMessages = listOf(UiText.StringResource(R.string.unknown_error)),
                        isProfileImageUploading = false
                    )
                }
            }
        }
    }

    private fun getUserProfileImage() {
        viewModelScope.launch(ioDispatcher) {
            firebaseRepository.getUserProfileImage().addOnCompleteListener { task ->
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
            dataStoreRepository.updateAppTheme(darkTheme)
            _uiState.update {
                it.copy(isDarkModeOn = darkTheme)
            }
        }
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

    fun setDynamicColor(dynamicColor: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            dataStoreRepository.updateDynamicColor(dynamicColor)
            _uiState.update {
                it.copy(isDynamicColorOn = dynamicColor)
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

    fun handleOnLogOutClick() {
        clearMovieLocalDatabase()
        firebaseAuth.signOut()
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