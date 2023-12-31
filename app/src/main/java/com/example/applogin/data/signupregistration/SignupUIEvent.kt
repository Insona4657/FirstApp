package com.example.applogin.data.signupregistration

sealed class SignupUIEvent{
    data class FirstNameChanged(val firstName:String) : SignupUIEvent()
    data class LastNameChanged(val lastName:String): SignupUIEvent()
    data class EmailChanged(val email:String) : SignupUIEvent()
    data class PasswordChanged(val password: String) : SignupUIEvent()
    data class PrivacyPolicyCheckBoxClicked(val status: Boolean) : SignupUIEvent()
    data class CompanyNameChanged(val companyName: String) : SignupUIEvent()
    data class UserStatusChanged(val userStatus: String) : SignupUIEvent()
    object RegisterButtonClicked : SignupUIEvent()
}
