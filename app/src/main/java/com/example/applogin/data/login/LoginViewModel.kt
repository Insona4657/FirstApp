package com.example.applogin.data.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applogin.data.rules.Validator
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.LoginListener
import com.example.applogin.loginflow.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel(){
    private val TAG = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())

    var allValidationsPassed = mutableStateOf(false)

    private var _loginInProgress = MutableLiveData(false)
    var loginInProgress: LiveData<Boolean> = _loginInProgress

    private var _loginFailed = MutableLiveData(false)
    var loginFailed: LiveData<Boolean> = _loginFailed

    private var _loginSuccess = MutableLiveData(false)
    var loginSuccess: LiveData<Boolean> = _loginSuccess

    fun onEvent(event: LoginUIEvent) {
        validateLoginUIDataWithRules()
        when(event){
            is LoginUIEvent.EmailChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
            }
            is LoginUIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
            }
            is LoginUIEvent.LoginButtonClicked -> {
                login()
            }
        }
        validateLoginUIDataWithRules()
    }

    private fun validateLoginUIDataWithRules() {
        val emailResult = Validator.validateEmail(
            email = loginUIState.value.email
        )
        val passwordResult = Validator.validatePassword(
            password = loginUIState.value.password
        )
        loginUIState.value = loginUIState.value.copy(
            emailError = emailResult.status,
            passwordError = passwordResult.status,
        )
        allValidationsPassed.value = emailResult.status && passwordResult.status
    }

    private fun login() {
        _loginInProgress.value = true
        Log.d(TAG, "Inside_login")
        val email = loginUIState.value.email
        val password = loginUIState.value.password

        // Check if email or password is empty
        if (email.isEmpty() || password.isEmpty()) {
            // Show toast message for empty email or password
            showToast("Email or password is empty")
            _loginInProgress.value = false
            return
        }
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "Inside_login_Success")
                Log.d(TAG, "${it.isSuccessful}")
                if(it.isSuccessful){
                    // Clear email and password fields
                    loginUIState.value = loginUIState.value.copy(email = "", password = "")
                    // Introduce a delay before navigating to home screen
                    setLoginSuccess(true) // Notify success
                    setLoginInProgress(false)
                    //AppRouter.navigateTo(Screen.HomeScreen)
                }else {
                    // Login failed, reset loginInProgress
                    setLoginInProgress(false)
                    }
            }
            .addOnFailureListener {
                Log.d(TAG, "Inside_login_Failure")
                Log.d(TAG, "${it.localizedMessage}")
                setLoginFailed(true)
                setLoginInProgress(false)
            }
    }
    fun setLoginSuccess(value: Boolean) {
        _loginSuccess.postValue(value)
    }

    fun setLoginFalse(value: Boolean) {
        _loginSuccess.postValue(value)
    }

    fun setLoginInProgress(value: Boolean) {
        _loginInProgress.postValue(value)
    }

    fun setLoginFailed(value: Boolean) {
        _loginFailed.postValue(value)
    }
    private fun showToast(message: String) {
        // Implement code to show toast message here
        // You can use Toast.makeText() to show the message
        // Example: Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        // Replace 'context' with the actual context reference
    }
}