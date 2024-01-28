package com.example.applogin.data.signupregistration

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.applogin.data.Company
import com.example.applogin.data.rules.Validator
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
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
    var companyNames: MutableState<List<String>> = mutableStateOf(emptyList())
    // MutableState for filtered company names
    var filteredCompanyNames: MutableState<List<String>> = mutableStateOf(emptyList())

    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        processCompanies()
    }
    // Function to filter company names based on the entered text
    fun filterCompanyNames(inputText: String) {
        // Access the value of companyNames
        val originalCompanyNames = companyNames.value
        // Update filteredCompanyNames based on the entered text
        filteredCompanyNames.value = originalCompanyNames.filter { companyName ->
            companyName.contains(inputText, ignoreCase = true)
        }
        // Log the output for debugging
        Log.d("FilterCompanyNames", "Input Text: $inputText")
        Log.d("FilterCompanyNames", "Original Company Names: $originalCompanyNames")
        Log.d("FilterCompanyNames", "Filtered Company Names: ${filteredCompanyNames.value}")
    }
    fun processCompanies() {
        val companyCollections = firestore.collection("all_test_customers")
        val allCompanyNames = mutableListOf<String>()

        companyCollections.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val keys = document.data.keys.toList()
                    allCompanyNames.addAll(keys)
                }

                val sortedCompanyNames = allCompanyNames.sorted()
                // Log the final list of all company names
                Log.d(TAG, "Final list of all company names: $sortedCompanyNames")

                // Now allCompanyNames list contains all keys across all documents
                // You can update your mutable state with this list if needed
                companyNames.value = sortedCompanyNames
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents: $exception")
            }
    }

    fun onEvent(event: SignupUIEvent, context : Context) {
        validateDataWithRules(context)
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
                signUp(context)
            }
            else -> {}
        }
        validateDataWithRules(context)
    }

    private fun signUp(context:Context) {
        Log.d(TAG, "Inside_signUp")
        // Validate each field
        val firstNameResult = Validator.validateFirstName(registrationUIState.value.firstName)
        val lastNameResult = Validator.validateLastName(registrationUIState.value.lastName)
        val emailResult = Validator.validateEmail(registrationUIState.value.email)
        val passwordResult = Validator.validatePassword(registrationUIState.value.password)
        var companyNameResult = Validator.validateCompanyName(registrationUIState.value.companyName, companyNames.value ?: emptyList())
        val userStateResult = Validator.validateUserState(registrationUIState.value.userState)

        // Check if any validation fails
        if (!firstNameResult.status) {
            // Show toast message for invalid first name
            Toast.makeText(context, "Invalid first name", Toast.LENGTH_SHORT).show()
            return
        }
        if (!lastNameResult.status) {
            // Show toast message for invalid last name
            Toast.makeText(context, "Invalid last name", Toast.LENGTH_SHORT).show()
            return
        }
        if (!emailResult.status) {
            // Show toast message for invalid email
            Toast.makeText(context, "Invalid email", Toast.LENGTH_SHORT).show()
            return
        }
        if (!companyNameResult.status) {
            // Show toast message for invalid company name
            Toast.makeText(context, "Invalid company name. Select from List", Toast.LENGTH_SHORT).show()
            return
        }
        if (!userStateResult.status) {
            // Show toast message for invalid user state
            Toast.makeText(context, "Invalid User Status Select User/Admin", Toast.LENGTH_SHORT).show()
            return
        }
        if (!passwordResult.status) {
            // Show toast message for invalid password
            Toast.makeText(context, "Invalid password", Toast.LENGTH_SHORT).show()
            return
        }

        createUserInFirebase(
            email = registrationUIState.value.email,
            password = registrationUIState.value.password,
            company = registrationUIState.value.companyName,
            firstname = registrationUIState.value.firstName,
            lastname = registrationUIState.value.lastName,
            userstatus = registrationUIState.value.userState,
            context = context)
    }

    private fun validateDataWithRules(context: Context) {

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

    private fun createUserInFirebase(
        email: String,
        password: String,
        firstname: String,
        lastname: String,
        company: String,
        userstatus: String,
        context: Context
    ) {
        signUpInProgress.value = true
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                signUpInProgress.value = false
                if (authResult.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                        // Successfully updated user profile
                    if (user != null) {
                        saveAdditionalUserInfoToFirestore(user.uid, firstname, lastname, company, userstatus)
                        Log.d(TAG, "User Found: ${user.uid}")
                        Toast.makeText(context, "User Successfully Registered", Toast.LENGTH_SHORT).show()
                        FirebaseAuth.getInstance().signOut() // Sign out the user after registration
                        AppRouter.navigateTo(Screen.LoginScreen)
                    }else{
                        Toast.makeText(context, "No User Found", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "User creation failed: No User Found")
                    }

                } else {
                    // Handle user creation failure
                    Toast.makeText(context, "${authResult.exception}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "User creation failed: ${authResult.exception}")
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Exception= ${e.message}")
                Log.d(TAG, "Exception= ${e.localizedMessage}")
            }
    }

    fun saveAdditionalUserInfoToFirestore(uid: String, firstname: String, lastname: String, company: String, userstatus: String) {
        // Assuming you have a reference to your Firestore database
        val db = FirebaseFirestore.getInstance()

        val userDocRef = db.collection("users").document(uid)

        val userData = hashMapOf(
            "changed_email" to false,
            "changed_pw" to false,
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

    /*
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
     */

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