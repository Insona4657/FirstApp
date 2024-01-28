package com.example.applogin.data.rules

import com.example.applogin.data.Company

object Validator {
    fun validateFirstName(fName:String) : ValidationResult{
        return ValidationResult(
            (!fName.isNullOrEmpty() && fName.length>=4)
        )
    }
    fun validateLastName(lName:String) : ValidationResult{
        return ValidationResult(
            (!lName.isNullOrEmpty() && lName.length>=4)
        )
    }
    fun validateEmail(email:String) : ValidationResult{
        val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")
        return ValidationResult(
            (!email.isNullOrEmpty() && emailRegex.matches(email))
        )
    }
    fun validatePassword(password:String) : ValidationResult{
        return ValidationResult(
            (!password.isNullOrEmpty() && password.length>=4)
        )
    }
    fun validateCompanyName(companyName:String, companies : List<String>) : ValidationResult{
        return ValidationResult(
            (!companyName.isNullOrEmpty() && companies.any { it == companyName })
        )
    }
    fun validateUserState(userStatus:String) : ValidationResult{
        return ValidationResult(
            (!userStatus.isNullOrEmpty() && (userStatus=="Admin" || userStatus=="User"))
        )
    }
    fun validatePrivacyPolicyAcceptance(statusValue: Boolean): ValidationResult {
        return ValidationResult(
            statusValue
        )
    }
}

data class ValidationResult(
    val status : Boolean = false
)