package com.example.applogin.screens

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.HeadingTextComponent
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.mainAppBar
import com.example.applogin.components.mainbackground
import com.example.applogin.components.navigationIcon
import com.example.applogin.data.Company
import com.example.applogin.data.WarrantySearchViewModel
import com.example.applogin.data.QueryResults
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.data.signupregistration.SignupViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import kotlinx.coroutines.launch
import androidx.compose.material3.Text as Text


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarrantyScreen(warrantySearchViewModel: WarrantySearchViewModel = viewModel(), signupViewModel: SignupViewModel = viewModel(), homeViewModel: HomeViewModel = viewModel()){
    val devices by warrantySearchViewModel.devices.observeAsState(emptyList())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedCompany by remember { mutableStateOf<Company?>(null) }
    var selectedCategory by remember { mutableStateOf ("")}

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
                mainAppBar(toolbarTitle = "Warranty",
                    logoutButtonClicked = {
                        homeViewModel.logout()
                    },
                    navigationIconClicked = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },

            ){ paddingValues ->
            Surface(modifier = Modifier
                .padding(paddingValues),
                //To enable the background function to work
                color = MaterialTheme.colorScheme.background,
                ) {
                Column(
                    modifier = Modifier,
                ) {
                    Surface(
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        //Background function
                        mainbackground()
                        Column(modifier = Modifier.fillMaxSize()) {
                            Spacer(modifier = Modifier.height(16.dp))

                            // Text Component to show header Warranty Checker
                            HeadingTextComponent(stringResource(R.string.warranty_checker))
                            Spacer(modifier = Modifier.height(20.dp))
                            categoryList(chosenItem={
                                    categoryChosen -> selectedCategory = categoryChosen })
                            //Function to display Company List
                            CompanyList(warrantySearchViewModel,
                                onCompanySelected = { company ->
                                        selectedCompany = company
                                },
                                category = selectedCategory)
                            Spacer(modifier = Modifier.height(20.dp))

                            Spacer(modifier = Modifier.height(20.dp))
                            // Display the devices associated with the selected company
                            DevicesList(devices,
                                warrantySearchViewModel,
                                selectedCompany = selectedCompany)

                        }
                    }
                }
            }
        }
    }
}

@Composable
fun categoryList(chosenItem: (String) -> Unit) {
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
                text = if (selectedChoice.isEmpty()) "Select Category to Search" else selectedChoice,
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
            } else {
                Icon(
                    imageVector = Icons.Default.ArrowDropUp,
                    contentDescription = "Clear Selection",
                    modifier = Modifier.size(24.dp)
                        .clickable {
                            if(selectedChoice.isNotEmpty()) {
                                selectedChoice = ""
                            } else {
                                expanded = !expanded
                            }
                        }
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                val choices = listOf(
                    "Company",
                    "IMEI Number",
                    "Product Model",
                    "Warranty End Date",
                    "Warranty Start Date"
                )

                choices.forEach { choice ->
                    DropdownMenuItem(
                        onClick = {
                            selectedChoice = choice
                            expanded = false
                            chosenItem(selectedChoice)
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
@SuppressLint("RestrictedApi")
@Composable
fun CompanyList(warrantySearchViewModel: WarrantySearchViewModel,
                // Callback to pass selected company
                onCompanySelected: (Company) -> Unit, category: String) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCompany by remember { mutableStateOf<Company?>(null) }
    var searchText by remember { mutableStateOf(TextFieldValue()) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top =16.dp,
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 4.dp)
                .clickable { expanded = !expanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search box
            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    // You can filter the list here based on the search text
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
                label = { Text("Search") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        modifier = Modifier.clickable {
                            expanded = !expanded
                        }
                    )
                },
                trailingIcon = {
                    val currentSearchText by rememberUpdatedState(searchText)
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Toggle Dropdown",
                        modifier = Modifier.clickable {
                            if (currentSearchText.text.isEmpty()) {
                                // Expand if searchText is empty
                                expanded = !expanded
                            } else {
                                // Clear searchText and then expand
                                searchText = TextFieldValue("")
                                expanded = true
                            }
                        }
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        expanded = !expanded
                    }
                )
            )
        }
        if (category == "Company") {
            AnimatedVisibility(visible = expanded) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var itemsToDisplay = warrantySearchViewModel.company_unique
                        .map { Company(customer = it) }
                        .distinctBy { it.customer }
                    itemsToDisplay.filter {
                        it.customer.contains(searchText.text, ignoreCase = true)
                    }.forEach { company ->
                        DropdownMenuItem(onClick = {
                            selectedCompany = company
                            expanded = false
                            //Updates searchbox with the company selected after it is selected
                            searchText = TextFieldValue(text = selectedCompany?.customer ?: "")
                            // Trigger the Firebase query when a company is selected
                            warrantySearchViewModel.queryDevicesByCompany(selectedCompany!!.customer)

                            // Invoke the callback with the selected company
                            onCompanySelected.invoke(selectedCompany!!)
                        }, text = {
                            Text(text = company.customer, modifier = Modifier.padding(16.dp))
                        })
                    }
                }
            }
            warrantySearchViewModel.processModel()
            warrantySearchViewModel.processWarranty()
            warrantySearchViewModel.processExtendedWarranty()
        }
        else if (category == "Product Model") {
            AnimatedVisibility(visible = expanded) {

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var itemsToDisplay = warrantySearchViewModel.model_unique
                        .map { Company(productModel = it) }
                        .distinctBy { it.productModel }
                    itemsToDisplay.filter {
                        it.productModel.contains(searchText.text, ignoreCase = true)
                    }.forEach { company ->
                        DropdownMenuItem(onClick = {
                            selectedCompany = company
                            expanded = false
                            //Updates searchbox with the company selected after it is selected
                            searchText = TextFieldValue(text = selectedCompany?.productModel ?: "")
                            // Trigger the Firebase query when a company is selected
                            warrantySearchViewModel.queryDevicesByCompany(selectedCompany!!.productModel)
                            // Invoke the callback with the selected company
                            onCompanySelected.invoke(selectedCompany!!)
                        }, text = {
                            Text(text = company.productModel, modifier = Modifier.padding(16.dp))
                        })
                    }
                }
            }
        }
        else if (category == "Warranty End Date") {
            AnimatedVisibility(visible = expanded) {

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var itemsToDisplay = warrantySearchViewModel.warranty_unique
                        .map { Company(warrantyEndDate = it) }
                        .distinctBy { it.warrantyEndDate }
                    itemsToDisplay.filter {
                        it.warrantyEndDate.contains(searchText.text, ignoreCase = true)
                    }.forEach { company ->
                        DropdownMenuItem(onClick = {
                            selectedCompany = company
                            expanded = false
                            //Updates searchbox with the company selected after it is selected
                            searchText = TextFieldValue(text = selectedCompany?.warrantyEndDate ?: "")
                            // Trigger the Firebase query when a company is selected
                            warrantySearchViewModel.queryDevicesByCompany(selectedCompany!!.warrantyEndDate)
                            // Invoke the callback with the selected company
                            onCompanySelected.invoke(selectedCompany!!)
                        }, text = {
                            Text(text = company.warrantyEndDate, modifier = Modifier.padding(16.dp))
                        })
                    }
                }
            }
        }
        else if (category == "IMEI Number") {
            AnimatedVisibility(visible = expanded) {

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var itemsToDisplay = warrantySearchViewModel.imei_unique
                        .map { Company(imeiNo = it) }
                        .distinctBy { it.imeiNo }
                    itemsToDisplay.filter {
                        it.imeiNo.toString().contains(searchText.text, ignoreCase = true)
                    }.forEach { company ->
                        DropdownMenuItem(onClick = {
                            selectedCompany = company
                            expanded = false
                            //Updates searchbox with the company selected after it is selected
                            searchText = TextFieldValue(text = selectedCompany?.imeiNo.toString() ?: "")
                            // Trigger the Firebase query when a company is selected
                            warrantySearchViewModel.queryDevicesByCompany(selectedCompany!!.imeiNo.toString())
                            // Invoke the callback with the selected company
                            onCompanySelected.invoke(selectedCompany!!)
                        }, text = {
                            Text(text = company.imeiNo.toString(), modifier = Modifier.padding(16.dp))
                        })
                    }
                }
            }
        }
        else if (category == "Warranty Start Date") {
            AnimatedVisibility(visible = expanded) {

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var itemsToDisplay = warrantySearchViewModel.extended_unique
                        .map { Company(extendedWarrantyDate = it) }
                        .distinctBy { it.extendedWarrantyDate }
                    itemsToDisplay.filter {
                        it.extendedWarrantyDate.contains(searchText.text, ignoreCase = true)
                    }.forEach { company ->
                        DropdownMenuItem(onClick = {
                            selectedCompany = company
                            expanded = false
                            //Updates searchbox with the company selected after it is selected
                            searchText = TextFieldValue(text = selectedCompany?.extendedWarrantyDate ?: "")
                            // Trigger the Firebase query when a company is selected
                            warrantySearchViewModel.queryDevicesByCompany(selectedCompany!!.extendedWarrantyDate)
                            // Invoke the callback with the selected company
                            onCompanySelected.invoke(selectedCompany!!)
                        }, text = {
                            Text(text = company.extendedWarrantyDate, modifier = Modifier.padding(16.dp))
                        })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicesList(devices: List<Company>,
                warrantySearchViewModel: WarrantySearchViewModel,
                selectedCompany: Company?) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier= Modifier.height(10.dp))
        // Display query results if available
    }
    LazyColumn(modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Summary",
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), // Adjust padding as needed
                textAlign = TextAlign.Center
            )
            Text(
                //Show Selected Company in the Summary
                text = selectedCompany?.customer ?:"",
                modifier = Modifier,
            )
        }
        warrantySearchViewModel.queryModel.value?.let { queryResult ->
            item {
                ShowResultMap(queryResult.resultMap)
            }
        }
        warrantySearchViewModel.queryDetailWarranty.value?.let { queryResult ->
            item {
                ShowResultMap(queryResult.resultMap)
            }
        }
        warrantySearchViewModel.queryDetailExtendedWarranty.value?.let { queryResult ->
            item {
                ShowResultMap(queryResult.resultMap)
            }
        }
        items(devices) { device ->
            ListItem({
                Column {
                    Text(
                        text = buildAnnotatedString {
                            append("Model:")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                                append(" ${device.productModel}")
                            }
                            append("\n")
                            append("IMEI:")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                                append(" ${device.imeiNo}")
                            }
                            append("\n")
                            append("Warranty End Date:")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                                append(" ${device.warrantyEndDate}")
                            }
                            append("\n")
                            append("Extended Warranty:")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                                append(" ${device.extendedWarrantyDate}")
                            }
                        }
                    )
                }
            })
        }
    }
}

@Composable
fun ShowResultMap(resultMap: Map<String, Int>) {
    // Display the resultMap
    for ((key, value) in resultMap) {
        Text(text = "$key: $value")
    }
}