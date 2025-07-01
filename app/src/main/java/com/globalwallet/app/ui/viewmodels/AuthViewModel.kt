package com.globalwallet.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globalwallet.app.data.repository.FirebaseRepository
import com.globalwallet.app.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isAuthenticated: Boolean = false,
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {
    
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    init {
        checkAuthState()
    }
    
    private fun checkAuthState() {
        val currentUser = firebaseRepository.getCurrentUser()
        if (currentUser != null) {
            _authState.value = AuthState(
                isAuthenticated = true,
                user = currentUser
            )
        }
    }
    
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            
            firebaseRepository.signInWithEmail(email, password)
                .onSuccess { user ->
                    _authState.value = AuthState(
                        isAuthenticated = true,
                        user = user
                    )
                }
                .onFailure { exception ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Authentication failed"
                    )
                }
        }
    }
    
    fun signUp(email: String, password: String, username: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, error = null)
            
            firebaseRepository.signUpWithEmail(email, password, username)
                .onSuccess { user ->
                    _authState.value = AuthState(
                        isAuthenticated = true,
                        user = user
                    )
                }
                .onFailure { exception ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Registration failed"
                    )
                }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            firebaseRepository.signOut()
            _authState.value = AuthState()
        }
    }
    
    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }
} 