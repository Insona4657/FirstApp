package com.example.applogin.data

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class WarrantySearchViewModel : ViewModel() {
    var companies: MutableLiveData<List<Company>> = MutableLiveData<List<Company>>()
    val devices: MutableLiveData<List<Company>> = MutableLiveData<List<Company>>()
    var queryModel: MutableLiveData<QueryResults> = MutableLiveData<QueryResults>()
    var queryDetailWarranty: MutableLiveData<QueryResults> = MutableLiveData<QueryResults>()
    var queryDetailExtendedWarranty: MutableLiveData<QueryResults> = MutableLiveData<QueryResults>()
    val imeisearchdevice: MutableLiveData<List<Company>> = MutableLiveData<List<Company>>()

    // LIST OF UNIQUE STRINGS to be searched in the search bar
    var company_unique: List<String> = emptyList()
    var model_unique: List<String> = emptyList()

    // Need to solve this Any issue with IMEI NUMBER
    var imei_unique: List<String> = emptyList()

    //To be modified in the future for specific date search
    var extended_unique: List<String> = emptyList()
    var warranty_unique : List<String> = emptyList()
    private lateinit var firestore: FirebaseFirestore

    //searchBox Text
    private val _searchText = mutableStateOf("")
    var searchText: State<String> = _searchText

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listentoCompanies()
    }


    //Function to Query database to check all Companies
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

                // Unique Customer Names
                val uniqueCustomerNames = uniqueCompanies.map { it.customer }.sorted()
                company_unique = uniqueCustomerNames
                Log.d("CompanyList", "Unique Companies: $company_unique")

                //Unique Model Names
                val uniqueModelNames = uniqueCompanies.map {it.productModel}.sorted()
                model_unique = uniqueModelNames
                Log.d("ModelList", "Unique Models: $model_unique")

                val uniqueImeiNumber = uniqueCompanies.map {it.imeiNo.toString() }.sorted()
                imei_unique = uniqueImeiNumber
                Log.d("ImeiList", "Sorted Unique Imei: $imei_unique")

                val uniqueWarranty = uniqueCompanies.map{it.warrantyEndDate}
                warranty_unique = uniqueWarranty
                Log.d("WarrantyList", "Unique Warranty: $warranty_unique")

                val uniqueExtendedWarranty = uniqueCompanies.map{it.extendedWarrantyDate}
                extended_unique = uniqueWarranty
                Log.d("ExtendedList", "Unique Extended Warranty: $extended_unique")
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
    //Function to Query Database to get Company Details for Specific Imei
    //Queries Entries to get company details for Imei Number selected
    fun queryEntryByImei(selectedImei: String) {
        firestore.collection("fullcustomers").whereEqualTo("IMEI NO", selectedImei).get()
            .addOnSuccessListener { snapshot ->
                val searchdevice = ArrayList<Company>()

                snapshot?.let {
                    for (document in snapshot.documents) {
                        val device = document.toObject(Company::class.java)
                        device?.let {
                            if (!searchdevice.contains(it)) {
                                searchdevice.add(it)
                            }
                        }
                    }
                }

                // Now perform a query for numbers
                firestore.collection("fullcustomers").whereEqualTo("IMEI NO", selectedImei.toLongOrNull()).get()
                    .addOnSuccessListener { numberSnapshot ->
                        numberSnapshot?.let {
                            for (document in numberSnapshot.documents) {
                                val device = document.toObject(Company::class.java)
                                device?.let {
                                    if (!searchdevice.contains(it)) {
                                        searchdevice.add(it)
                                    }
                                }
                            }
                        }

                        imeisearchdevice.value = searchdevice
                        Log.e("FIRESTORE QUERY FOR IMEI", "CHECK QUERY OUTPUT ${imeisearchdevice.value}")
                    }
                //imeisearchdevice.value = searchdevice
                //Log.e("FIRESTORE QUERY FOR IMEI", "CHECK QUERY OUTPUT ${imeisearchdevice.value}")
            }
            .addOnFailureListener { exception ->
                // Handle failure
                Log.e("FirestoreQuery", "Error querying data: $exception")
            }
    }
    //Function to update SearchText
    fun setSearchText(value: String) {
        _searchText.value = value
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
        Log.e("queryModel", "CHECK QUERY OUTPUT ${queryModel.value}")
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
        Log.e("queryDetailWarranty", "CHECK QUERY OUTPUT ${queryDetailWarranty.value}")
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
        Log.e("queryExtendedWarranty", "CHECK QUERY OUTPUT ${queryDetailExtendedWarranty.value}")
    }
}