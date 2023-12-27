package com.example.applogin.data

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NewWarrantySearchViewModel : ViewModel() {
    //Initialize the firestore
    private lateinit var firestore: FirebaseFirestore
    private val deviceList: MutableList<Company> = mutableListOf()

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }
    fun getCompanyList(): List<Company> {
        return deviceList
    }

    fun checkImeiNumber(imeiToCheck: String): Any {
        for (company in deviceList) {
            if (company.imeiNo == imeiToCheck) {
                // If IMEI number matches, return the specific Company
                return company
            }
        }
        // If no match is found, return "No Device Found"
        return "No Device Found"
    }

    fun categorizeDevicesByWarrantyStatus(selectedDate: MutableState<LocalDate>): Map<String, Map<String, List<Company>>> {
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        val devicesBeforeWarrantyEnd = mutableListOf<Company>()
        val devicesAfterWarrantyEnd = mutableListOf<Company>()

        for (company in deviceList) {
            val warrantyEndDate = LocalDate.parse(company.warrantyEndDate, dateFormatter)

            if (selectedDate.value.isAfter(warrantyEndDate)) {
                // Date has passed, group by model for devices with expired warranty
                devicesBeforeWarrantyEnd.add(company)
            } else {
                // Date has not passed, group by model for devices with active warranty
                devicesAfterWarrantyEnd.add(company)
            }
        }

        val groupedDevices = mapOf(
            "Devices with Expired Warranty" to devicesBeforeWarrantyEnd.groupBy { it.productModel },
            "Devices with Active Warranty" to devicesAfterWarrantyEnd.groupBy { it.productModel }
        )

        return groupedDevices
    }

    fun sumDevicesByModel(modelName: String): Map<String, Map<String, Int>> {
        val devicesForModel = deviceList.filter { it.productModel == modelName }

        val result = mutableMapOf<String, MutableMap<String, Int>>()

        for (device in devicesForModel) {
            val warrantyEndDate = device.warrantyEndDate
            val extendedWarrantyDate = device.extendedWarrantyDate

            val warrantyEndDateMap = result.getOrPut(warrantyEndDate) { mutableMapOf() }
            warrantyEndDateMap[modelName] = (warrantyEndDateMap[modelName] ?: 0) + 1

            val extendedWarrantyDateMap = result.getOrPut(extendedWarrantyDate) { mutableMapOf() }
            extendedWarrantyDateMap[modelName] = (extendedWarrantyDateMap[modelName] ?: 0) + 1
        }

        return result
    }

    private fun getSpecificCompanyData(companyName: String) {
        val companyCollections = firestore.collection("all_test_customers")
        companyCollections.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (companyName == document.data.keys.firstOrNull()) {
                        Log.d(TAG, "$companyName")
                        val companyDetailsList = document.data.values.firstOrNull()
                        if (companyDetailsList is List<*>) {
                            val size = companyDetailsList.size
                            Log.d(TAG, "Size of companyDetailsList: $size")
                            for (companyDetails in companyDetailsList) {
                                if (companyDetails is Map<*, *>) {
                                    // Access individual fields within the map
                                    val imeiNo = companyDetails["IMEI NO"] as? String ?: ""
                                    val productModel = companyDetails["PRODUCT MODEL"] as? String ?: ""
                                    val extendedWarrantyDate = companyDetails["EXTENDED WARRANTY DATE"] as? String ?: ""
                                    val warrantyEndDate = companyDetails["WARRANTY END DATE"] as? String ?: ""

                                    // Create a Company object or perform other actions as needed
                                    val company = Company(
                                        customer = companyName,
                                        extendedWarrantyDate = extendedWarrantyDate,
                                        imeiNo = imeiNo,
                                        productModel = productModel,
                                        warrantyEndDate = warrantyEndDate
                                    )
                                    deviceList.add(company)
                                }
                            }
                        }
                    }
                }
            }
    }
    private fun getAllCompanyData() {
        val companyCollections = firestore.collection("all_test_customers")
        companyCollections.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val array = document.data
                    Log.d(TAG, "$array")
                    val companyName = document.data.keys.firstOrNull()
                    if (companyName != null) {
                        Log.d(TAG, "Company Name: $companyName")
                    }
                    // If you want to access the list of maps containing company details
                    val companyDetailsList = document.data.values.firstOrNull()
                    if (companyDetailsList is List<*>) {
                        // Now you can iterate through the list and access individual company details
                        for (companyDetails in companyDetailsList) {
                            if (companyDetails is Map<*, *>) {
                                // Access individual fields within the map
                                val imeiNo = companyDetails["IMEI NO"] as? String ?: ""
                                val productModel = companyDetails["PRODUCT MODEL"] as? String ?: ""
                                val extendedWarrantyDate =
                                    companyDetails["EXTENDED WARRANTY DATE"] as? String ?: ""
                                val warrantyEndDate =
                                    companyDetails["WARRANTY END DATE"] as? String ?: ""

                                // Create a Company object or perform other actions as needed
                                val company = companyName?.let {
                                    Company(
                                        customer = it,
                                        extendedWarrantyDate = extendedWarrantyDate,
                                        imeiNo = imeiNo,
                                        productModel = productModel,
                                        warrantyEndDate = warrantyEndDate
                                    )
                                }
                                Log.d(TAG, "Company Details: $company")
                                if (company != null) {
                                    deviceList.add(company)
                                }
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Log.e(TAG, "Error getting documents: $exception")
            }
    }

    fun checkCompanyName() {
        val firestore = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser?.uid
        val usersCollection = firestore.collection("users")


        if (currentUser != null) {
            Log.d(TAG, "CurrentUser ID: $currentUser")

            usersCollection.document(currentUser).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "DocumentSnapshot exists")

                        val userData = documentSnapshot.toObject(UserData::class.java)
                        if (userData != null) {
                            Log.d(TAG, "User Data fetched successfully")

                            //Access User Data to check company details
                            if (userData.company == "Syndes") {
                                Log.d(TAG, "User is Syndes Company")
                                // Create logic to download all Company details
                                getAllCompanyData()
                            } else {
                                getSpecificCompanyData(userData.company)
                            }
                    }
                }
            }
        }
    }


}