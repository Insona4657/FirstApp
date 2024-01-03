package com.example.applogin.data

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NewWarrantySearchViewModel : ViewModel() {
    //Initialize the firestore
    private lateinit var firestore: FirebaseFirestore
    private var totalDeviceList: MutableList<Company> = mutableListOf()

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        checkCompanyName()
    }
    fun getCompanyList(): List<Company> {
        return totalDeviceList
    }

    fun checkImeiNumber(imeiToCheck: String): Company? {
        for (company in totalDeviceList) {
            if (company.imeiNo == imeiToCheck) {
                // If IMEI number matches, return the specific Company
                Log.d(TAG, "Company Match Found")
                return company
            }
        }
        // If no match is found, return null
        Log.d(TAG, "Company Match Not Found")
        return null
    }
    fun getUniqueModel(): List<String> {
        return totalDeviceList.distinctBy { it.productModel }.map { it.productModel }
    }

    fun getuniqueImei(imeiToCheck: String): List<String> {
        val uniqueImeiNumbers = totalDeviceList.map { it.imeiNo?.toString() }.toSet()
        val similarImeiNumbers = uniqueImeiNumbers.filter { it?.contains(imeiToCheck) == true }
        return similarImeiNumbers.filterNotNull()
    }

    fun categorizeDevicesByWarrantyStatus(selectedDate: LocalDate): Map<String, Map<String, List<Company>>> {

        if (selectedDate !is LocalDate) {
            Log.e("Error in LocalDate Format", "Invalid data type for selectedDate. Expected LocalDate.")
            return emptyMap() // Return an empty map or handle the error accordingly
        }

        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        val devicesBeforeWarrantyEnd = mutableListOf<Company>()
        val devicesAfterWarrantyEnd = mutableListOf<Company>()

        for (company in totalDeviceList) {
            val warrantyEndDateString = company.warrantyEndDate

            if (warrantyEndDateString.isEmpty()) {
                Log.w("EndDate is Empty", "Empty warranty end date for company: ${company.customer}")
                continue // Skip this company and move to the next one
            }
            val warrantyEndDate: LocalDate =
                LocalDate.parse(warrantyEndDateString, dateFormatter)

        /*catch (e: Exception) {
                Log.e("YourTag", "Error parsing warranty end date for company: ${company.customer}", e)
                null // Handle the parsing error, mark warrantyEndDate as null
            }

         */

            if (selectedDate.isAfter(warrantyEndDate)) {
                // Date has passed, group by model for devices with expired warranty
                devicesBeforeWarrantyEnd.add(company)
            } else {
                // Date has not passed, group by model for devices with active warranty
                devicesAfterWarrantyEnd.add(company)
            }
        }

        val groupedDevices = mapOf(
            "Devices with Expired Warranty" to devicesBeforeWarrantyEnd.groupBy { it.customer },
            "Devices with Active Warranty" to devicesAfterWarrantyEnd.groupBy { it.customer }
        )

        return groupedDevices
    }

    fun sumDevicesByModel(modelName: String): Map<String, Map<String, Int>> {
        val devicesForModel = totalDeviceList.filter { it.productModel == modelName }

        val result = mutableMapOf<String, MutableMap<String, Int>>()

        for (device in devicesForModel) {
            val warrantyEndDate = device.warrantyEndDate
            //val extendedWarrantyDate = device.extendedWarrantyDate

            val warrantyEndDateMap = result.getOrPut(warrantyEndDate) { mutableMapOf() }
            warrantyEndDateMap[modelName] = (warrantyEndDateMap[modelName] ?: 0) + 1

        }
        return result
    }


    private fun getSpecificCompanyData(companyName: String) {
        val companyCollections = firestore.collection("all_test_customers")
        val deviceList: MutableList<Company> = mutableListOf()
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
                                    val imeiNoRaw = companyDetails["IMEI NO"]
                                    val imeiNo = when (imeiNoRaw) {
                                        is Number -> imeiNoRaw.toString() // Convert to string if it's a number
                                        is String -> imeiNoRaw // Keep as is if it's already a string
                                        else -> ""
                                    }
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
                            totalDeviceList = deviceList
                            val sizeOfTotalDeviceList = totalDeviceList.size
                            Log.d(TAG, "Size of totalDeviceList: $sizeOfTotalDeviceList")
                        }
                    }
                }
            }
    }
    private fun getAllCompanyData() {
        val companyCollections = firestore.collection("all_test_customers")
        val deviceList: MutableList<Company> = mutableListOf()
        companyCollections.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // Get Company Name only to be saved
                    val companyName = document.data.keys.firstOrNull()
                    // If you want to access the list of maps containing company details
                    val companyDetailsList = document.data.values.firstOrNull()
                    if (companyDetailsList is List<*>) {
                        // Now you can iterate through the list and access individual company details
                        for (companyDetails in companyDetailsList) {
                            if (companyDetails is Map<*, *>) {
                                // Access individual fields within the map
                                val imeiNoRaw = companyDetails["IMEI NO"]
                                val imeiNo = when (imeiNoRaw) {
                                    is Number -> imeiNoRaw.toString() // Convert to string if it's a number
                                    is String -> imeiNoRaw // Keep as is if it's already a string
                                    else -> ""
                                }
                                val productModel = companyDetails["PRODUCT MODEL"] as? String ?: ""
                                val extendedWarrantyDate =
                                    companyDetails["EXTENDED WARRANTY DATE"] as? String ?: ""
                                val warrantyEndDate =
                                    companyDetails["WARRANTY END DATE"] as? String ?: ""

                                // Create a Company object or perform other actions as needed
                                val company = companyName?.let {
                                    Company(
                                        customer = companyName,
                                        extendedWarrantyDate = extendedWarrantyDate,
                                        imeiNo = imeiNo,
                                        productModel = productModel,
                                        warrantyEndDate = warrantyEndDate
                                    )
                                }
                                //Log.d(TAG, "Company Details: $company")
                                if (company != null) {
                                    deviceList.add(company)
                                }
                            }
                        }
                        totalDeviceList = deviceList
                        val sizeOfTotalDeviceList = totalDeviceList.size
                        //Log.d(TAG, "Size of totalDeviceList: $sizeOfTotalDeviceList")
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
                                Log.d(TAG, "User is Normal Company")
                                getSpecificCompanyData(userData.company)
                            }
                    }
                }
            }
        }
    }


}