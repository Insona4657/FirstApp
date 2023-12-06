package com.example.applogin.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.RequestPage
import androidx.lifecycle.ViewModel
import com.example.applogin.R
import com.example.applogin.data.home.HomeViewModel

//List of Products to be Displayed
class ProductViewModel: ViewModel() {
    private val TAG = ProductViewModel::class.simpleName

    val productList = listOf<ProductItem>(
        ProductItem(
            brand = "Samsung",
            name = "Galaxy X Cover 6 Pro",
            model = "SM-G736UZKEXAA",
            imageResId = R.drawable.samsung_galaxy_xcover6_pro,
        ),
        ProductItem(
            brand = "Samsung",
            name = "Galaxy X Cover 5",
            model = "SM-G525F, SM-G525F/DS, SM-G525N",
            imageResId = R.drawable.samsung_galaxy_xcover5,
        ),
        ProductItem(
            brand = "Samsung",
            name = "Galaxy Tab Active 4 Pro",
            model = "SM-T636BZKAXME",
            imageResId = R.drawable.samsung_galaxy_tab_active4_pro,
        ),
        ProductItem(
            brand = "Newland",
            name = "Desktop Scanner",
            model = "FR4080",
            imageResId = R.drawable.newland_fr4080_desktop_scanner,
        ),
        ProductItem(
            brand = "Newland",
            name = "Hand Scanner",
            model = "HR32 Marlin BT",
            imageResId = R.drawable.newland_hr32_marlin_bt_sd,
        ),
        ProductItem(
            brand = "Newland",
            name = "Mobile Computer Scanner",
            model = "MT67 Sei",
            imageResId = R.drawable.newland_mt67_sei,
        ),
        ProductItem(
            brand = "Zebra",
            name = "Handheld Scanner",
            model = "3600 Series",
            imageResId = R.drawable.zebra_3600_series,
        ),
        ProductItem(
            brand = "Zebra",
            name = "Zebra Industrial Tablets",
            model = "ET40ET45",
            imageResId = R.drawable.zebra_et40et45,
        ),
        ProductItem(
            brand = "Zebra",
            name = "Ultra-Rugged Mobile Computer",
            model = "MC9300",
            imageResId = R.drawable.zebra_mc9300,
        ),
        ProductItem(
            brand = "Zebra",
            name = "Mobile Computers",
            model = "TC73, TC78",
            imageResId = R.drawable.zebra_tc73_tc78,
        ),
        ProductItem(
            brand = "Zebra",
            name = "Mobile Printer",
            model = "ZD220230",
            imageResId = R.drawable.zebra_zd220230,
        ),
        ProductItem(
            brand = "Zebra",
            name = "Mobile Printer",
            model = "ZQ500",
            imageResId = R.drawable.zebra_zq500,
        )
    )
}