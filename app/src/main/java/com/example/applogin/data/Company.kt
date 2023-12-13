package com.example.applogin.data

import androidx.compose.ui.graphics.vector.ImageVector
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
    var warrantyEndDate: String = "",

) {
    fun getPropertyValue(criteria: String): Any? {
        return when (criteria) {
            "WARRANTY END DATE" -> warrantyEndDate
            "PRODUCT MODEL" -> productModel
            "EXTENDED WARRANTY DATE" -> extendedWarrantyDate
            else -> null
        }
    }
}

data class QueryResults(
    var totalCount: String = "",
    var groupVariable: String = "",
    var resultMap: Map<String, Int> = emptyMap()
)

data class NavigationItem(
    val title : String,
    val description : String,
    val itemId: String,
    val icon: ImageVector,
)

data class ProductItem(
    val brand : String,
    val category : String,
    val name: String,
    val model : String,
    val imageResId: Int,
)

data class ProductDetail(
    val brandImage : Int,
    val name : String,
    val model : String,
    val imageResId: Int,
    val specification : List<String>
)
