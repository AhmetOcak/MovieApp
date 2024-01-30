package com.ahmetocak.movieapp.presentation.home.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmetocak.movieapp.R
import com.ahmetocak.movieapp.common.DialogUiEvent
import com.ahmetocak.movieapp.common.Response
import com.ahmetocak.movieapp.common.helpers.UiText
import com.ahmetocak.movieapp.data.repository.firebase.FirebaseRepository
import com.ahmetocak.movieapp.data.repository.movie.MovieRepository
import com.ahmetocak.movieapp.model.firebase.auth.Auth
import com.google.firebase.auth.FirebaseAuth
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
    firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
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

     private fun clearMovieLocalDatabase(onSuccess: () -> Unit) {
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
}

data class ProfileUiState(
    val profileImgUrl: String = "",
    val userEmail: String = "",
    val userMessages: List<UiText> = emptyList(),
    val deleteAccountDialogUiEvent: DialogUiEvent = DialogUiEvent.InActive
)