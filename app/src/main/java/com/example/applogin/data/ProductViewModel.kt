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

    val productDetailList = listOf<ProductDetail>(
        ProductDetail(
            brandImage = R.drawable.samsung_logo,
            brand = "Samsung",
            name = "Galaxy Tab Active 4 Pro",
            model = "SM-T636BZKAXME",
            imageResId = R.drawable.samsung_galaxy_tab_active4_pro,
            specification = listOf(
                "GSM / HSPA / LTE / 5G",
                "IP68 dust/water resistant (up to 1.5m for 30 min)",
                "Drop-to-concrete resistance from up to 1 meter",
                "10.1 inches, 295.8 cm2 (~71.5% screen-to-body ratio)",
                "1920 x 1200 pixels, 16:10 ratio (~224 ppi density)",
                "Corning Gorilla Glass 5",
                "Qualcomm SM7325 Snapdragon 778G 5G (6 nm)",
                "Octa-core (1x2.4 GHz Cortex-A78 & 3x2.2 GHz Cortex-A78 & 4x1.8 GHz Cortex-A55)",
                "Adreno 642L",
                "64GB 4GB RAM, 128GB 6GB RAM",
                "13 MP, f/1.9, AF"
            )
        ),
        ProductDetail(
        brandImage = R.drawable.samsung_logo,
        brand = "Samsung",
        name = "Galaxy XCover6 Pro",
        model = "SM-G736UZKEXAA",
        imageResId = R.drawable.samsung_galaxy_xcover6_pro,
        specification = listOf(
            "GSM / HSPA / LTE / 5G",
            "IP68 dust/water resistant (up to 1.5m for 35 min)",
            "Drop-to-concrete resistance from up to 1.5 meter",
            "6.6 inches, 104.9 cm2 (~77.8% screen-to-body ratio)",
            "1080 x 2408 pixels, 20:9 ratio (~400 ppi density)",
            "Corning Gorilla Glass Victus+",
            "Qualcomm SM7325 Snapdragon 778G 5G (6 nm)",
            "Octa-core (1x2.4 GHz Cortex-A78 & 3x2.2 GHz Cortex-A78 & 4x1.9 GHz Cortex-A55)",
            "Adreno 642L",
            "128GB 6GB RAM",
            "50 MP, f/1.8, (wide), 1/2.76 inch, 0.64µm, PDAF",
            "8 MP, f/2.2, 123˚ (ultrawide), 1/4.0 inch, 1.12µm",
            )
        ),
        ProductDetail(
            brandImage = R.drawable.samsung_logo,
            brand = "Samsung",
            name = "Galaxy XCover5",
            model = "SM-G525F, SM-G525F/DS, SM-G525N",
            imageResId = R.drawable.samsung_galaxy_xcover5,
            specification = listOf(
                "GSM / HSPA / LTE",
                "IP68 dust/water resistant (up to 1.5m for 30 min)",
                "5.3 inches, 71.3 cm2 (~67.7% screen-to-body ratio)",
                "720 x 1480 pixels, 18.5:9 ratio (~311 ppi density)",
                "Exynos 850 (8nm)",
                "Octa-core (4x2.0 GHz Cortex-A55 & 4x2.0 GHz Cortex-A55)",
                "Mali-G52",
                "64GB 4GB RAM",
                "eMMC 5.1",
                "16 MP, f/1.8, PDAF"
            )
        ),
        ProductDetail(
            brandImage = R.drawable.newland_logo,
            brand = "Newland",
            name = "Desktop Scanner",
            model = "FR4080",
            imageResId = R.drawable.newland_fr4080_desktop_scanner,
            specification = listOf(
                "1280 x 800 CMOS",
                "1D & 2D Barcode Scanning",
                "IP52-rated Sealing",
                "1.2m Drop Resistance",
                "83 x 81 x 148mm",
                "USB, RS-232 Interface",
                "Wired",
                "293g Weight",
            )
        ),
        ProductDetail(
            brandImage = R.drawable.newland_logo,
            brand = "Newland",
            name = "Hand Scanner",
            model = "HR32 Marlin BT",
            imageResId = R.drawable.newland_hr32_marlin_bt_sd,
            specification = listOf(
                "1280 x 800 CMOS",
                "1D & 2D Barcode Scanning",
                "IP52-rated Sealing",
                "≥12 hours of continuous operation(at 6sec/scan)",
                "113.5 x 73.3 x 159mm",
                "USB Interface",
                "2400 mAh lithium-ion battery",
                "217g Weight",
            )
        ),
        ProductDetail(
            brandImage = R.drawable.newland_logo,
            brand = "Newland",
            name = "Mobile Computer Scanner",
            model = "MT67 Sei",
            imageResId = R.drawable.newland_mt67_sei,
            specification = listOf(
                "GSM / HSPA / LTE / 4G",
                "1280 x 800 CMOS",
                "1D & 2D Barcode Scanning",
                "NFC RFID, Bluetooth 5.0, ",
                "162 x 68 x 17 mm",
                "4 inches, 800 x 480 pixels",
                "Corning Gorilla Glass",
                "64GB 4GM RAM",
                "2.0GHz octa-core 64-bit processor",
                "13 MP/ 5MP Rear/Front Camera, AutoFocus",
                "250g Weight 3.8V, 4800mAh Battery"
            )
        ),
        ProductDetail(
            brandImage = R.drawable.zebra_logo,
            brand = "Zebra",
            name = "Handheld Scanner",
            model = "3600 Series",
            imageResId = R.drawable.zebra_3600_series,
            specification = listOf(
                "1D & 2D Barcode Scanning",
                "3m Drop-to-concrete resistance",
                "IP65/IP68 sealing",
                "Scan from 7.6cm-21m away",
                "Wifi & Bluetooth Connection",
                "1280 x 800 Image Sensor",
                "70000+ Scans on One Charge",
                "334g/436g Wired/Wireless",
                "18.5cm x 7.6cm x 13.2/14.2cm Wired/Wireless"
            )
        ),
        ProductDetail(
            brandImage = R.drawable.zebra_logo,
            brand = "Zebra",
            name = "Zebra Industrial Tablets",
            model = "ET40ET45",
            imageResId = R.drawable.zebra_et40et45,
            specification = listOf(
                "GSM / HSPA / LTE / 5G",
                "IP65, Wifi & Bluetooth Enabled",
                "8/10 inch, 1280x800/1920x1200 Display",
                "8/10 inch 485g/690g weight",
                "Corning Gorilla Glass",
                "1.2m Drop-to-concrete resistance",
                "Qualcomm SM7325 Snapdragon SM6375",
                "Octa-Core (8): 2.2 GHz (2) and 1.8 GHz (6)",
                "64GB 4GB RAM / 128GB 8GB RAM",
                "13/5MP Back/Front Camera, AutoFocus",
                "1D/2D Barcode Scanning",
                "6100mAh/7600mAh/3400mAh(Hot Swappable battery"
            )
        ),
        ProductDetail(
            brandImage = R.drawable.zebra_logo,
            brand = "Zebra",
            name = "Ultra-Rugged Mobile Computer",
            model = "MC9300",
            imageResId = R.drawable.zebra_mc9300,
            specification = listOf(
                "GSM / HSPA / LTE / 5G",
                "IP67/IP65 per applicable IEC sealing specifications",
                "Up to 3.1m Drop-to-concrete resistance",
                "4.3 inches, WVGA 800x480, Color Display",
                "1920 x 1200 pixels, 16:10 ratio (~224 ppi density)",
                "Corning Gorilla Glass",
                "Qualcomm Snapdragon™ 660 octa-core, 2.2 GHz",
                "32GB 4GB RAM",
                "13/5 MP Back/Front Camera with Autofocus",
                "1D/2D Scanning Supported",
                "Wifi/NFC/Bluetooth Supported",
                "3.6V, 7000mAh with hotswap battery backup"
            )
        ),
        ProductDetail(
            brandImage = R.drawable.zebra_logo,
            brand = "Zebra",
            name = "Mobile Computers",
            model = "TC73, TC78",
            imageResId = R.drawable.zebra_tc73_tc78,
            specification = listOf(
                "GSM / HSPA / LTE / 5G",
                "IP65 and IP68 with battery per applicable IEC sealing specifications",
                "Built to Handle 3.05m Drop-to-concrete",
                "6.0 inch Full HD Display (1080 X 2160)",
                "Corning Gorilla Glass 5",
                "Qualcomm 6490 octa-core, 2.7 GHz",
                "64GB 4GB RAM, 128GB 8GB RAM",
                "16MP Camera, 1D & 2D Barcode Scanning"
            )
        ),
        ProductDetail(
            brandImage = R.drawable.zebra_logo,
            brand = "Zebra",
            name = "Mobile Printer",
            model = "ZD220230",
            imageResId = R.drawable.zebra_zd220230,
            specification = listOf(
                "Wifi, Bluetooth & USB Connectivity",
                "Thermal Transfer / Direct Thermal Printing",
                "1-inch to 4-Inch Thermal Printer up to 6'/s printing",
                "203 dpi/8dots per mm",
                "267mm x 197mm x 191mm & 1.1kg",
                "128MB Flash Storage",
            )
        ),
        ProductDetail(
            brandImage = R.drawable.zebra_logo,
            brand = "Zebra",
            name = "Mobile Printer",
            model = "ZQ500",
            imageResId = R.drawable.zebra_zq500,
            specification = listOf(
                "Wifi, Bluetooth & USB Connectivity",
                "Direct Thermal Printing",
                "Tolerance of Multiple Drops from 2m to Concrete",
                "Easy to Read LCD Screen",
                "3250mAh Battery",
                "1-inch to 4-Inch Thermal Printer up to 6'/s printing",
                "203 dpi/8dots per mm",
                "150mm x 120mm x 62mm, 0.79KG",
                "256MB Ram, 512MB Flash Storage",
            )
        ),
    )


    // Original unfiltered list
    private var originalList = productList

    // MutableState for observed filtered products
    private var _filteredProducts = mutableStateOf(originalList)

    fun setBrandFilter(brand: String) {
        if (brand == "All Brands") {
            _filteredProducts.value = originalList
        } else {
            _filteredProducts.value = originalList.filter { it.brand == brand }
        }
    }

    fun setCategoryFilter(category: String) {
        if (category == "All Products") {
            _filteredProducts.value = originalList
        } else {
            _filteredProducts.value = originalList.filter { it.category == category }
        }
    }

    // Function to get the current list based on filters
    fun getCurrentProductList(): List<ProductItem> {
        return _filteredProducts.value
    }

    fun getProductDetailByModel(model: String): ProductDetail? {
        return productDetailList.find { it.model == model }
    }

}