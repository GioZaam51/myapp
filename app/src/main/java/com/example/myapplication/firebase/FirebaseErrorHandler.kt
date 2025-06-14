package com.example.myapplication.firebase

import com.google.firebase.auth.*

object FirebaseErrorHandler {

    fun getAuthErrorMessage(e: Exception): String {
        return when (e) {
            is FirebaseAuthInvalidUserException -> "El usuario no está registrado."
            is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos."
            is FirebaseAuthWeakPasswordException -> "La contraseña es demasiado débil."
            is FirebaseAuthUserCollisionException -> "Este correo ya está registrado."
            is FirebaseAuthException -> "Error de autenticación: ${e.message}"
            else -> "Ocurrió un error inesperado: ${e.message}"
        }
    }
}
