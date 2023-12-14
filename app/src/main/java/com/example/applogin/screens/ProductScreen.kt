package com.example.applogin.screens

import android.content.ContentValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.HeadingTextComponent
import com.example.applogin.components.MainPageTopBackground
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.SmallTextComponent
import com.example.applogin.components.mainAppBar
import com.example.applogin.components.mainbackground
import com.example.applogin.data.ProductDetail
import com.example.applogin.data.ProductItem
import com.example.applogin.data.ProductViewModel
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(homeViewModel: HomeViewModel = viewModel(), productViewModel: ProductViewModel = viewModel()){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val brandChoices = listOf("Samsung", "Zebra", "Newland", "No Filter")
    val categoryChoices = listOf("Smartphone", "Tablet", "Scanner", "Printer", "Mobile Computer", "No Filter")
    val (selectedProduct, setSelectedProduct) = remember { mutableStateOf<String?>(null) }

    ModalNavigationDrawer(
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet{
                Column{
                    NavigationDrawerHeader()
                    NavigationDrawerBody(navigationDrawerItems = homeViewModel.navigationItemsList, onClick = {
                        Log.d(ContentValues.TAG, "Inside NavigationDrawer")
                        Log.d(ContentValues.TAG, "Inside ${it.itemId} ${it.title}")
                        AppRouter.navigateTo(AppRouter.getScreenForTitle(it.title))
                    })
                }
            }
        }, drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                mainAppBar(toolbarTitle = "Product Screen",
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
                        //TODO
                    }
                )
            },
            ){ paddingValues ->
            Surface(modifier = Modifier
                .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                MainPageTopBackground(
                    topimage =R.drawable.top_background,
                    middleimage = R.drawable.middle_background,
                    bottomimage = R.drawable.bottom_background)
                Column(
                    modifier = Modifier,
                ) {
                    HeadingTextComponent(introText = "Products")

                    //DropdownList for Category filtering
                    SmallTextComponent("Filter by Category")
                    productCategory(categoryChoices, "Category") { selectedCategory ->
                        // Update the filter in the view model and capture the returned list
                        productViewModel.setCategoryFilter(selectedCategory)
                    }
                    // Dropdownlist for Brand filtering
                    SmallTextComponent("Filter By Brand")
                    productCategory(brandChoices, "Brand") { selectedBrand ->
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

// Get product details as a formatted text
@Composable
fun getProductDetailsText(product: ProductDetail): String {
    val specifications = product.specification.joinToString("\n")
    return "Name: ${product.name}\nBrandImage: ${product.brandImage}\nModel: ${product.model}\n\nSpecifications:\n$specifications"
}

@Composable
fun ProductList(filteredProducts: List<ProductItem>, onItemClick: (String) -> Unit) {

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),

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
                            .height(250.dp)
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .shadow(4.dp)
            .clickable {
                //Gets the product item name and then link to the specification
                onItemClick(productItem) // Trigger click event
            }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxHeight()  // Added to make the height equal
        ) {
            Image(
                painter = painterResource(id = productItem.imageResId),
                contentDescription = null, // Provide a content description if needed
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .clipToBounds(),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Name: ${productItem.name}")

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Brand: ${productItem.brand}")
            Text(text = "Model: ${productItem.model}")
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
            .padding(16.dp)
            .background(Color.White) // Set background color
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(4.dp)) // Add border
            .padding(16.dp), // Add padding,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Text Content
            Text(
                text = if (selectedChoice.isEmpty()) filterType else selectedChoice,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                color = Color.Black // Customize text color as needed
            )
            if (!expanded) {
                // Trailing Icon
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Trailing Icon",
                    modifier = Modifier.size(24.dp)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    productDetail: ProductDetail
) {

    /*
    AlertDialog(
        containerColor = Color.Transparent, // Set background color to transparent
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        },
        title = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.middle_background),
                    contentDescription = "Background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().fillMaxWidth()
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = productDetail.name)
                    // Icon
                    Icon(
                        painter = painterResource(id = productDetail.brandImage),
                        contentDescription = "Brand Icon",
                        modifier = Modifier
                            .size(48.dp)
                            .padding(8.dp)
                    )
                    // Specifications
                    productDetail.specification.forEach { spec ->
                        Text(text = spec, modifier = Modifier
                            .padding(4.dp)
                            .size(50.dp))
                    }
                }
            }
        }
    )*/

}

@Preview
@Composable
fun DefaultPreviewProductScreen() {
    ProductScreen()
}