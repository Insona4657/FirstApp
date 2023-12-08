package com.example.applogin.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.RequestPage
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.applogin.R
import com.example.applogin.data.home.HomeViewModel

//List of Products to be Displayed
class ProductViewModel: ViewModel() {
    private val TAG = ProductViewModel::class.simpleName

    val productList = listOf<ProductItem>(
        ProductItem(
            brand = "Samsung",
            category = "Smartphone",
            name = "Galaxy XCover6 Pro",
            model = "SM-G736UZKEXAA",
            imageResId = R.drawable.samsung_galaxy_xcover6_pro,
        ),
        ProductItem(
            brand = "Samsung",
            category = "Smartphone",
            name = "Galaxy XCover5",
            model = "SM-G525F, SM-G525F/DS, SM-G525N",
            imageResId = R.drawable.samsung_galaxy_xcover5,
        ),
        ProductItem(
            brand = "Samsung",
            category = "Tablet",
            name = "Galaxy Tab Active 4 Pro",
            model = "SM-T636BZKAXME",
            imageResId = R.drawable.samsung_galaxy_tab_active4_pro,
        ),
        ProductItem(
            brand = "Newland",
            category = "Scanner",
            name = "Desktop Scanner",
            model = "FR4080",
            imageResId = R.drawable.newland_fr4080_desktop_scanner,
        ),
        ProductItem(
            brand = "Newland",
            category = "Scanner",
            name = "Hand Scanner",
            model = "HR32 Marlin BT",
            imageResId = R.drawable.newland_hr32_marlin_bt_sd,
        ),
        ProductItem(
            brand = "Newland",
            category = "Mobile Computer",
            name = "Mobile Computer Scanner",
            model = "MT67 Sei",
            imageResId = R.drawable.newland_mt67_sei,
        ),
        ProductItem(
            brand = "Zebra",
            category = "Scanner",
            name = "Handheld Scanner",
            model = "3600 Series",
            imageResId = R.drawable.zebra_3600_series,
        ),
        ProductItem(
            brand = "Zebra",
            category = "Tablet",
            name = "Zebra Industrial Tablets",
            model = "ET40ET45",
            imageResId = R.drawable.zebra_et40et45,
        ),
        ProductItem(
            brand = "Zebra",
            category = "Mobile Computer",
            name = "Ultra-Rugged Mobile Computer",
            model = "MC9300",
            imageResId = R.drawable.zebra_mc9300,
        ),
        ProductItem(
            brand = "Zebra",
            category = "Mobile Computer",
            name = "Mobile Computers",
            model = "TC73, TC78",
            imageResId = R.drawable.zebra_tc73_tc78,
        ),
        ProductItem(
            brand = "Zebra",
            category = "Printer",
            name = "Mobile Printer",
            model = "ZD220230",
            imageResId = R.drawable.zebra_zd220230,
        ),
        ProductItem(
            brand = "Zebra",
            category = "Printer",
            name = "Mobile Printer",
            model = "ZQ500",
            imageResId = R.drawable.zebra_zq500,
        )
    )

    // Original unfiltered list
    private var originalList = productList

    // MutableState for observed filtered products
    private var _filteredProducts = mutableStateOf(originalList)

    fun setBrandFilter(brand: String) {
        if (brand == "No Filter") {
            _filteredProducts.value = originalList
        } else {
            _filteredProducts.value = originalList.filter { it.brand == brand }
        }
    }

    fun setCategoryFilter(category: String) {
        if (category == "No Filter") {
            _filteredProducts.value = originalList
        } else {
            _filteredProducts.value = originalList.filter { it.category == category }
        }
    }

    // Function to get the current list based on filters
    fun getCurrentProductList(): List<ProductItem> {
        return _filteredProducts.value
    }
}