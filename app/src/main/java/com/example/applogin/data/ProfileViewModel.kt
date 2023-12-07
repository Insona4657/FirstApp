package com.example.applogin.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileViewModel : ViewModel() {
    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> get() = _user

    init {
        // Initialize the ViewModel and fetch the current user details
        FirebaseAuth.getInstance().currentUser?.let {
            _user.value = it
        }
    }
}