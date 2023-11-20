package com.example.applogin.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class HomeViewModel : ViewModel() {
    var companies: MutableLiveData<List<Company>> = MutableLiveData<List<Company>>()

    private lateinit var firestore : FirebaseFirestore
    init{
        firestore = FirebaseFirestore.getInstance()
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        listentoCompanies()
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
                companies.value =allCompanies
            }
        }
    }
}