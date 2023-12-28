package com.example.applogin.screens

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.MyFirebaseMessagingService
import com.example.applogin.R
import com.example.applogin.components.MainPageTopBackground
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.ProductCompanyComponent
import com.example.applogin.components.ProductTextComponent
import com.example.applogin.components.TwoImageBackground
import com.example.applogin.components.mainAppBar
import com.example.applogin.data.ProductDetail
import com.example.applogin.data.ProductItem
import com.example.applogin.data.ProductViewModel
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(homeViewModel: HomeViewModel = viewModel(),
                  productViewModel: ProductViewModel = viewModel()
){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val brandChoices = listOf("Samsung", "Zebra", "Newland", "All Brands")
    val categoryChoices = listOf("Smartphone", "Tablet", "Scanner", "Printer", "Mobile Computer", "All Products")
    val (selectedProduct, setSelectedProduct) = remember { mutableStateOf<String?>(null) }
    ModalNavigationDrawer(
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet{
                Column{
                    NavigationDrawerHeader()
                    NavigationDrawerBody(navigationDrawerItems = homeViewModel.navigationItemsList) {
                        Log.d(ContentValues.TAG, "Inside NavigationDrawer")
                        Log.d(ContentValues.TAG, "Inside ${it.itemId} ${it.title}")
                        AppRouter.navigateTo(AppRouter.getScreenForTitle(it.title))
                    }
                }
            }
        }, drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                mainAppBar(toolbarTitle = "",
                    logoutButtonClicked = {
                        homeViewModel.logout()
                    },
                    navigationIconClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    barcodeIconClicked = {
                        AppRouter.navigateTo(AppRouter.getScreenForTitle("Barcode Scanner"))
                    },
                    notificationIconClicked = {
                        AppRouter.navigateTo(AppRouter.getScreenForTitle("Inbox"))
                    },
                )
            },
            ){ paddingValues ->
            Surface(modifier = Modifier
                .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                MainPageTopBackground(
                    topimage =R.drawable.product_category_banner,
                    middleimage = R.drawable.middle_background,
                    bottomimage = R.drawable.bottom_background)
                Column(
                    modifier = Modifier.padding(start=10.dp, end = 10.dp, top = 20.dp, bottom = 50.dp),
                ) {
                    //Spacer(modifier = Modifier.height(5.dp))
                    ProductCompanyComponent(introText = "SYNDES")
                    ProductTextComponent(introText = "Product Category")
                    //DropdownList for Category filtering
                    productCategory(categoryChoices, "Select Product Category to Search") { selectedCategory ->
                        // Update the filter in the view model and capture the returned list
                        productViewModel.setCategoryFilter(selectedCategory)
                    }
                    // Dropdownlist for Brand filtering
                    productCategory(brandChoices, "Select Brand to Search") { selectedBrand ->
                        // Update the filter in the view model and capture the returned list
                        productViewModel.setBrandFilter(selectedBrand)
                    }
                    // Get the current list of products based on filters
                    val filteredProducts = productViewModel.getCurrentProductList()

                    // Product List Carousel with updated productList
                    ProductList(filteredProducts = filteredProducts) { productItem ->
                        setSelectedProduct(productItem)
                    }
                    // Show the product details dialog when a product is selected
                    if (selectedProduct != null) {
                        productViewModel.getProductDetailByModel(selectedProduct)?.let {
                            AlertDialogExample(
                                onDismissRequest = { setSelectedProduct(null) },
                                onConfirmation = {
                                    // Add logic if needed
                                    setSelectedProduct(null)
                                },
                                productDetail = it
                            )
                        }
                    }
                }
            }
        }

    }
}


@Composable
fun ProductList(filteredProducts: List<ProductItem>, onItemClick: (String) -> Unit) {

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),

        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(filteredProducts.chunked(2)) { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                for (productItem in rowItems) {
                    ProductItemBox(
                        productItem = productItem, modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    ){
                        onItemClick(productItem.model)
                    }
                }
            }
        }
        item{ Spacer(modifier = Modifier.height(10.dp))}
    }
}

@Composable
fun ProductItemBox(
    productItem: ProductItem,
    modifier: Modifier = Modifier,
    onItemClick: (ProductItem) -> Unit //Send the ProductItem name into
) {
    val codecProFont = FontFamily(
        Font(R.font.codec_pro_regular, FontWeight.Normal, FontStyle.Normal)
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, Color.White, shape = RoundedCornerShape(20.dp))
            .padding(4.dp)
            .clickable {
                //Gets the product item name and then link to the specification
                onItemClick(productItem) // Trigger click event
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()  // Added to make the height equal
        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                Text(text = productItem.category,
                    style = TextStyle (
                        fontSize = 15.sp,
                        fontFamily = codecProFont,
                        fontStyle = FontStyle.Normal,
                        color = Color(255, 165, 0)),
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                )
                Text(text = productItem.name,
                    style = TextStyle (
                        fontSize = 15.sp,
                        fontFamily = codecProFont,
                        fontStyle = FontStyle.Normal,)
                )
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)) {
                Image(
                    painter = painterResource(id = productItem.imageResId),
                    contentDescription = null, // Provide a content description if needed
                    modifier = Modifier
                        .height(120.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .clipToBounds()
                        .graphicsLayer(
                            scaleX = 1.2f,
                            scaleY = 1.2f
                        ),
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}

@Composable
fun productCategory(choices: List<String>, filterType : String, onCategorySelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedChoice by remember { mutableStateOf("") } // Default is empty
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(start = 16.dp, end = 16.dp, top = 5.dp, bottom = 5.dp)
            .border(1.dp, Color.Transparent, shape = RoundedCornerShape(15.dp)) // Add border
            .clip(RoundedCornerShape(15.dp))
            .background(Color(254, 175, 8)), // Set background color,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
        ) {
            // Text Content
            Text(
                text = if (selectedChoice.isEmpty()) filterType else selectedChoice,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                color = Color.White, // Customize text color as needed
                style = TextStyle (
                        fontSize = 15.sp,
                fontStyle = FontStyle.Normal,
            ),
            )
            if (!expanded) {
                // Trailing Icon
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Trailing Icon",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp)),
            ) {
                val choices = choices

                choices.forEach { choice ->
                    DropdownMenuItem(
                        onClick = {
                            selectedChoice = choice
                            expanded = false
                            onCategorySelected(choice)
                        },
                        text = {
                            Text(
                                text = choice,
                                color = Color.Black // Change text color as needed
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    productDetail: ProductDetail
) {
    Box(modifier = Modifier.clip(RoundedCornerShape(25.dp))) {
        Dialog(
            onDismissRequest = {
                onDismissRequest()
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Card(
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth(0.95f)
            ) {
                Box(
                    modifier = Modifier
                        .padding(5.dp)
                ) {
                    TwoImageBackground(
                        topimage = R.drawable.product_detail_banner,
                        middleimage = R.drawable.product_category_background
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = productDetail.brand + ' ' + productDetail.name,
                                modifier = Modifier,
                                style = TextStyle.Default.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp // Adjust the font size as needed
                                ),
                                color = Color.White
                            )
                            Box(modifier = Modifier
                                .fillMaxWidth(),
                                contentAlignment = Alignment.Center

                            ) {
                                Image(
                                    painter = painterResource(productDetail.imageResId),
                                    contentDescription = "Product Image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .size(250.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                        LazyColumn(modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clip(RoundedCornerShape(25.dp))
                        ){
                            items(productDetail.specification) { spec ->
                                Text(
                                    text = spec,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxWidth()
                                )
                                Divider(
                                    color = Color.Gray,
                                    thickness = 1.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreviewProductScreen() {
    ProductScreen()
}