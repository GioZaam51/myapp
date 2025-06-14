package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.myapplication.firebase.FirebaseErrorHandler


class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _error.value = "Por favor llena todos los campos"
            return
        }

        _isLoading.value = true

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    _loginSuccess.value = true
                } else {
                    val exception = task.exception
                    val errorMsg = FirebaseErrorHandler.getAuthErrorMessage(exception ?: Exception("Error desconocido"))
                    _error.value = errorMsg
                }
            }
    }
}
