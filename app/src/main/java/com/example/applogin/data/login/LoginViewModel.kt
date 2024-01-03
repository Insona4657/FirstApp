package com.example.applogin.data.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.applogin.data.rules.Validator
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel(){
    private val TAG = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())

    var allValidationsPassed = mutableStateOf(false)

    private var _loginInProgress = MutableLiveData(false)
    var loginInProgress: LiveData<Boolean> = _loginInProgress

    private var _loginFailed = MutableLiveData(false)
    var loginFailed: LiveData<Boolean> = _loginFailed

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

        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                Log.d(TAG, "Inside_login_Success")
                Log.d(TAG, "${it.isSuccessful}")

                if(it.isSuccessful){
                    setLoginInProgress(false)
                    AppRouter.navigateTo(Screen.HomeScreen)
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Inside_login_Failure")
                Log.d(TAG, "${it.localizedMessage}")
                setLoginFailed(true)
            }
    }

    fun setLoginInProgress(value: Boolean) {
        _loginInProgress.postValue(value)
    }

    fun setLoginFailed(value: Boolean) {
        _loginFailed.postValue(value)
    }
}