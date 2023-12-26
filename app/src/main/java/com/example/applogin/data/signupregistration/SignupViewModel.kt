package com.example.applogin.data.signupregistration

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.applogin.data.Company
import com.example.applogin.data.rules.Validator
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class SignupViewModel : ViewModel() {
    private val TAG = SignupViewModel::class.simpleName
    var registrationUIState = mutableStateOf(RegistrationUIState())
    var allValidationsPassed = mutableStateOf(false)
    var signUpInProgress = mutableStateOf( false)
    var companies: MutableLiveData<List<Company>> = MutableLiveData<List<Company>>()
    private lateinit var firestore : FirebaseFirestore

    private val _companyName = mutableStateOf("")
    val companyName: State<String> = _companyName

    private val _userStatusOptions = listOf("User", "Admin")

    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listentoCompanies()
    }
    fun onEvent(event: SignupUIEvent) {
        validateDataWithRules()
        when(event){
            is SignupUIEvent.FirstNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    firstName = event.firstName
                )
                printState()
            }
            is SignupUIEvent.LastNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    lastName = event.lastName
                )
                printState()
            }
            is SignupUIEvent.EmailChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    email = event.email
                )
                printState()
            }
            is SignupUIEvent.PasswordChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    password = event.password
                )
                printState()
            }
            is SignupUIEvent.PrivacyPolicyCheckBoxClicked -> {
                registrationUIState.value = registrationUIState.value.copy(
                    privacyPolicyAccepted = event.status
                )
                printState()
            }
            is SignupUIEvent.CompanyNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    companyName = event.companyName
                )
                printState()
            }
            is SignupUIEvent.UserStatusChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    userState = event.userStatus
                )
                printState()
            }
            is SignupUIEvent.RegisterButtonClicked -> {
                Log.d(TAG, "Registration Button Clicked")
                signUp()
            }
        }
        validateDataWithRules()
    }

    private fun signUp() {
        Log.d(TAG, "Inside_signUp")
        printState()
        createUserInFirebase(
            email = registrationUIState.value.email,
            password = registrationUIState.value.password,
            company = registrationUIState.value.companyName,
            firstname = registrationUIState.value.firstName,
            lastname = registrationUIState.value.lastName,
            userstatus = registrationUIState.value.userState,
        )
    }

    private fun validateDataWithRules() {
        val fNameResult = Validator.validateFirstName(
            fName = registrationUIState.value.firstName
        )
        val lNameResult = Validator.validateLastName(
            lName = registrationUIState.value.lastName
        )
        val emailResult = Validator.validateEmail(
            email = registrationUIState.value.email
        )
        val passwordResult = Validator.validatePassword(
            password = registrationUIState.value.password
        )
        Log.d(TAG, "Inside_validateDataWithRules")
        Log.d(TAG, "fNameResult= $fNameResult")
        Log.d(TAG, "lNameResult= $lNameResult")
        Log.d(TAG, "emailResult= $emailResult")
        Log.d(TAG, "passwordResult = $passwordResult")

        registrationUIState.value = registrationUIState.value.copy(
            firstNameError = fNameResult.status,
            lastNameError = lNameResult.status,
            emailError = emailResult.status,
            passwordError = passwordResult.status,
        )

        allValidationsPassed.value = fNameResult.status && lNameResult.status &&
            emailResult.status && passwordResult.status
    }

    private fun printState() {
        Log.d(TAG, "Inside_printState")
        Log.d(TAG, registrationUIState.toString())
    }

    /*
    private fun createUserInFirebase(email:String, password:String) {

        signUpInProgress.value = true

        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "Inside_OnCompleteListener")
                Log.d(TAG," isSuccessful = ${it.isSuccessful}")

                signUpInProgress.value = false
                if(it.isSuccessful){
                    AppRouter.navigateTo(Screen.LoginScreen)
                }
            }
            .addOnFailureListener {
                Log.d(TAG,"Inside_OnFailureListener")
                Log.d(TAG,"Exception = ${it.message}")
                Log.d(TAG,"Exception= ${it.localizedMessage}")
            }
    }
     */
    private fun createUserInFirebase(
        email: String,
        password: String,
        firstname: String,
        lastname: String,
        company: String,
        userstatus: String
    ) {
        signUpInProgress.value = true

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                signUpInProgress.value = false

                if (authResult.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser

                    // Update the user's profile with additional details
                    val profileUpdates = userProfileChangeRequest {
                        displayName = "$firstname $lastname"
                    }

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileUpdateTask ->
                            if (profileUpdateTask.isSuccessful) {
                                // Successfully updated user profile
                                saveAdditionalUserInfoToFirestore(user.uid, firstname, lastname, company, userstatus)
                                AppRouter.navigateTo(Screen.LoginScreen)
                            } else {
                                // Handle profile update failure
                                Log.d(TAG, "Profile update failed: ${profileUpdateTask.exception}")
                            }
                        }
                } else {
                    // Handle user creation failure
                    Log.d(TAG, "User creation failed: ${authResult.exception}")
                }
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Exception= ${e.message}")
                Log.d(TAG, "Exception= ${e.localizedMessage}")
            }
    }

    private fun saveAdditionalUserInfoToFirestore(uid: String, firstname: String, lastname: String, company: String, userstatus: String) {
        // Assuming you have a reference to your Firestore database
        val db = FirebaseFirestore.getInstance()

        val userDocRef = db.collection("users").document(uid)

        val userData = hashMapOf(
            "firstname" to firstname,
            "lastname" to lastname,
            "company" to company,
            "status" to userstatus
        )

        userDocRef.set(userData)
            .addOnSuccessListener {
                // Successfully stored additional user data to Firestore
                Log.d(TAG, "Successfully stored additional user data: $userstatus, $company, $firstname+$lastname")
            }
            .addOnFailureListener {
                // Handle failure
                Log.d(TAG, "Failed to store additional user data: $it")
            }
    }

    private fun listentoCompanies() {
        firestore.collection("testcustomers").addSnapshotListener {
                snapshot, e->
            if (e != null) {
                Log.w("Listen failed", e)
                return@addSnapshotListener
            }
            // If we reached this point, there is not an error
            snapshot?.let {
                val allCompanies = ArrayList<Company>()
                // it shows all documents in general
                val documents = snapshot.documents
                documents.forEach {
                    val company = it.toObject(Company::class.java)
                    company?.let {
                        allCompanies.add(it)
                    }
                }
                companies.value = allCompanies
            }
        }
    }

    fun logout() {
        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()

        val authStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                Log.d(TAG, "Inside sign out Success")
                AppRouter.navigateTo(Screen.LoginScreen)
            } else {
                Log.d(TAG, "Inside sign out is not complete")

            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)
    }
}