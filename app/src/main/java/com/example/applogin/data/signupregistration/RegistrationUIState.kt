package com.example.applogin.data.signupregistration

data class RegistrationUIState(
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var password: String = "",
    var privacyPolicyAccepted: Boolean = false,
    var companyName: String = "",
    var userState: String = "",

    var firstNameError: Boolean = false,
    var lastNameError: Boolean = false,
    var emailError: Boolean = false,
    var passwordError: Boolean = false,
    var privacyPolicyError: Boolean = false,
    var companyNameError: Boolean = false,
)

enum class UserState {
    NORMAL_USER,
    ADMIN
}