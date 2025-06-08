package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun registerUser(email: String, password: String) {
        _showLoader.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _showLoader.value = false
                if (task.isSuccessful) {
                    _registrationSuccess.value = true
                } else {
                    _errorMessage.value = task.exception?.message ?: "Error desconocido"
                }
            }
    }
}
