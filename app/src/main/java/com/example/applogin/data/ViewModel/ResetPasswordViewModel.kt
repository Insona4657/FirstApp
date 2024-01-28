package com.example.applogin.data.ViewModel

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class ResetPasswordViewModel(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) : ViewModel() {
    fun resetPassword(
        userEmail: String,
        onResetCompleted: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.sendPasswordResetEmail(userEmail)
            .addOnSuccessListener {
                // Password reset email sent successfully
                onResetCompleted.invoke()
            }
            .addOnFailureListener { e ->
                // If there is an error during password reset
                onError.invoke(e.message ?: "Password reset failed.")
            }
    }
}