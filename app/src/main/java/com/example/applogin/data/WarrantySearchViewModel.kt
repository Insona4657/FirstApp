package com.example.applogin.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class WarrantySearchViewModel : ViewModel() {
    //val liveData: MutableLiveData<List<QueryResults>> = MutableLiveData<List<QueryResults>>()
    var companies: MutableLiveData<List<Company>> = MutableLiveData<List<Company>>()
    val devices: MutableLiveData<List<Company>> = MutableLiveData<List<Company>>()
    var queryProduct: MutableLiveData<List<QueryResults>> = MutableLiveData<List<QueryResults>>()
    var queryWarranty: MutableLiveData<List<QueryResults>> = MutableLiveData<List<QueryResults>>()
    var queryExtendedWarranty: MutableLiveData<List<QueryResults>> = MutableLiveData<List<QueryResults>>()
    var queryModel: MutableLiveData<QueryResults> = MutableLiveData<QueryResults>()
    //var queryResults: MutableLiveData<List<QueryResults>> = MutableLiveData<List<QueryResults>>()
    var queryDetailWarranty: MutableLiveData<QueryResults> = MutableLiveData<QueryResults>()
    var queryDetailExtendedWarranty: MutableLiveData<QueryResults> = MutableLiveData<QueryResults>()
    //var queryCompany: MutableLiveData<QueryResults> = MutableLiveData<QueryResults>()
    var company_unique: List<String> = emptyList()
    private lateinit var firestore: FirebaseFirestore

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listentoCompanies()
    }

    private fun listentoCompanies() {
        firestore.collection("fullcustomers").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("Listen failed", e)
                return@addSnapshotListener
            }
            // If we reached this point, there is not an error
            snapshot?.let {
                val uniqueCompanies = HashSet<Company>()

                for (document in snapshot.documents) {
                    val company = document.toObject(Company::class.java)
                    company?.let {
                        uniqueCompanies.add(it)
                    }
                }

                // Convert the HashSet back to a list and update LiveData
                val allCompanies = uniqueCompanies.toList()
                companies.value = allCompanies.toList()

                val uniqueCustomerNames = uniqueCompanies.map { it.customer }
                company_unique = uniqueCustomerNames
                Log.d("CompanyList", "Unique Companies: $company_unique")
            }
        }
    }

    fun queryDevicesByCompany(companyName: String) {
        // Perform a query to update devices
        firestore.collection("fullcustomers").whereEqualTo(
            "CUSTOMER", companyName
        ).get().addOnSuccessListener { snapshot ->
            val devicesList = ArrayList<Company>()
            snapshot?.let {
                for (document in snapshot.documents) {
                    val device = document.toObject(Company::class.java)
                    device?.let {
                        if (!devicesList.contains(it)) {
                            devicesList.add(it)
                        }
                    }
                }
                devices.value = devicesList
            }
        }
    }


    fun queryWarranty(companyName: String, group_by: String) {
        // Perform a query to update devices
        firestore.collection("fullcustomers")
            .whereEqualTo("CUSTOMER", companyName)
            .whereEqualTo("WARRANTY END DATE", group_by)
            .get()
            .addOnSuccessListener { snapshot ->
                val count = snapshot.size()
                val result = QueryResults(count.toString(), group_by)

                // Append the new result to the existing list
                queryWarranty.value = listOf(result)
            }
    }
    fun queryExtendedWarranty(companyName: String, group_by: String) {
        // Perform a query to update devices
        firestore.collection("fullcustomers")
            .whereEqualTo("CUSTOMER", companyName)
            .whereEqualTo("EXTENDED WARRANTY DATE", group_by)
            .get()
            .addOnSuccessListener { snapshot ->
                val count = snapshot.size()
                val result = QueryResults(count.toString(), group_by)

                // Append the new result to the existing list
                queryExtendedWarranty.value = listOf(result)
            }
    }
    fun queryProduct(companyName: String, group_by: String) {
        // Perform a query to update devices
        firestore.collection("fullcustomers")
            .whereEqualTo("CUSTOMER", companyName)
            .whereEqualTo("PRODUCT MODEL", group_by)
            .get()
            .addOnSuccessListener { snapshot ->
                val count = snapshot.size()
                val result = QueryResults(count.toString(), group_by)

                // Append the new result to the existing list
                queryProduct.value = listOf(result)
            }
    }


    fun processModel() {
        val devicesList = devices.value ?: emptyList()

        // Create a resultMap to store counts for each unique item
        val resultMap = HashMap<String, Int>()

        // Iterate through the devicesList
        for (device in devicesList) {
            // Example: Assuming you want to count based on the 'productModel' field
            val uniqueItem = device.productModel

            // Update the resultMap
            if (uniqueItem != null) {
                resultMap[uniqueItem] = resultMap.getOrDefault(uniqueItem, 0) + 1
            }
        }

        // Create a QueryResults object and update the LiveData
        val queryoutcome = QueryResults(
            totalCount = devicesList.size.toString(),
            groupVariable = "productModel", // Change this to the actual variable name
            resultMap = resultMap
        )

        queryModel.value = queryoutcome
    }
    fun processWarranty() {
        val devicesList = devices.value ?: emptyList()

        // Create a resultMap to store counts for each unique item
        val resultMap = HashMap<String, Int>()

        // Iterate through the devicesList
        for (device in devicesList) {
            // Example: Assuming you want to count based on the 'productModel' field
            val uniqueItem = device.warrantyEndDate

            // Update the resultMap
            if (uniqueItem != null) {
                resultMap[uniqueItem] = resultMap.getOrDefault(uniqueItem, 0) + 1
            }
        }

        // Create a QueryResults object and update the LiveData
        val queryResult = QueryResults(
            totalCount = devicesList.size.toString(),
            groupVariable = "warrantyEndDate", // Change this to the actual variable name
            resultMap = resultMap
        )

        queryDetailWarranty.value = queryResult
    }
    fun processExtendedWarranty() {
        val devicesList = devices.value ?: emptyList()

        // Create a resultMap to store counts for each unique item
        val resultMap = HashMap<String, Int>()

        // Iterate through the devicesList
        for (device in devicesList) {
            // Example: Assuming you want to count based on the 'productModel' field
            val uniqueItem = device.extendedWarrantyDate

            // Update the resultMap
            if (uniqueItem != null) {
                resultMap[uniqueItem] = resultMap.getOrDefault(uniqueItem, 0) + 1
            }
        }

        // Create a QueryResults object and update the LiveData
        val queryResult = QueryResults(
            totalCount = devicesList.size.toString(),
            groupVariable = "extendedWarrantyDate", // Change this to the actual variable name
            resultMap = resultMap
        )

        queryDetailExtendedWarranty.value = queryResult
    }
}