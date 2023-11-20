package com.example.applogin.data

import com.google.firebase.firestore.PropertyName

data class Company(
    @get:PropertyName("CUSTOMER") @set:PropertyName("CUSTOMER")
    var customer: String = "",

    @get:PropertyName("EXTENDED WARRANTY DATE") @set:PropertyName("EXTENDED WARRANTY DATE")
    var extendedWarrantyDate: String = "",

    @get:PropertyName("IMEI NO") @set:PropertyName("IMEI NO")
    var imeiNo: Any = "",

    @get:PropertyName("PRODUCT MODEL") @set:PropertyName("PRODUCT MODEL")
    var productModel: String = "",

    @get:PropertyName("WARRANTY END DATE") @set:PropertyName("WARRANTY END DATE")
    var warrantyEndDate: String = ""


    // Add other properties as neede
)