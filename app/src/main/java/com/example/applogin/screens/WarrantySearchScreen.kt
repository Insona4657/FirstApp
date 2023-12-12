package com.example.applogin.screens

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.HeadingTextComponent
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.SmallTextComponent
import com.example.applogin.components.mainAppBar
import com.example.applogin.components.mainbackground
import com.example.applogin.data.Company
import com.example.applogin.data.WarrantySearchViewModel
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.launch
import java.time.LocalDate
import androidx.compose.material3.Text as Text


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarrantyScreen(warrantySearchViewModel: WarrantySearchViewModel = viewModel(), homeViewModel: HomeViewModel = viewModel()){
    val devices by warrantySearchViewModel.devices.observeAsState(emptyList())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedCompany by remember { mutableStateOf<Company?>(null) }
    var selectedCategory by remember { mutableStateOf ("")}
    var selectedImei by remember { mutableStateOf<String?>(null) }

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
                    },
                    barcodeIconClicked = {
                        //requestCameraAndStartScanner()
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
                                category = selectedCategory,
                                onImeiSelected = { imei ->
                                    selectedImei = imei
                                })
                            Spacer(modifier = Modifier.height(20.dp))
                            Spacer(modifier = Modifier.height(20.dp))
                            // Display the devices associated with the selected company
                            DevicesList(devices,
                                warrantySearchViewModel,
                                selectedCategory = selectedCategory,
                                selectedCompany = selectedCompany,
                                selectedImei = selectedImei)

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
                text = selectedChoice.ifEmpty { "Select Category to Search" },
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
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            if (selectedChoice.isNotEmpty()) {
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
@Composable
fun datepicker(closeSelection:UseCaseState.() -> Unit) {

    var selectedDate = remember { mutableStateOf<LocalDate?>(LocalDate.now()) }

    CalendarDialog(
        state = rememberUseCaseState(visible = true, true, onCloseRequest = closeSelection),
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true
        ),
        selection = CalendarSelection.Date(
            selectedDate = selectedDate.value
        ) { newDate ->
            selectedDate.value = newDate
        },
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = selectedDate.value.toString() ?: "",
            onValueChange = {},
            label = { Text("Selected Date") },
            trailingIcon = {
                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "Select Date"
                    )
                }
            },
            readOnly = true,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

    }
}
/*
    val calendarState = UseCaseState()
    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
        ),
        selection = CalendarSelection.Date { date ->
            Log.d("SelectedDate", "$date")
        })


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value =
            onValueChange = { },
            label = { Text("Selected Date") },
            trailingIcon = {
                IconButton(
                    onClick = {
                        calendarState.show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = stringResource(R.string.cd_select_date)
                    )
                }
            }

*/


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RestrictedApi")
@Composable
fun CompanyList(warrantySearchViewModel: WarrantySearchViewModel,
                // Callback to pass selected company
                onCompanySelected: (Company) -> Unit, category: String, onImeiSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCompany by remember { mutableStateOf<Company?>(null) }
    var selectedImei by remember { mutableStateOf<String>("")}
    var searchBox by remember {mutableStateOf ("")}
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(
            start = 16.dp,
        )
        .clickable { expanded = !expanded },
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        if (category == "Company") {
            searchBox() { searchChange ->
                searchBox = searchChange
            }
            //LaunchedEffect(searchBox){
            //    expanded = !searchBox.isNullOrEmpty()
            //}
            AnimatedVisibility(visible = expanded) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    var itemsToDisplay = warrantySearchViewModel.company_unique
                        .map { Company(customer = it) }
                        .distinctBy { it.customer }
                    itemsToDisplay.filter {
                        it.customer.contains(searchBox, ignoreCase = true)
                    }.forEach { company ->
                        DropdownMenuItem(onClick = {
                            selectedCompany = company
                            expanded = !expanded
                            //Updates searchbox with the company selected after it is selected
                            searchBox = selectedCompany?.customer ?: ""
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
        } else if (category == "IMEI Number") {
            searchBox() { searchChange ->
                searchBox = searchChange
            }
            //LaunchedEffect(searchBox){
            //    expanded = !searchBox.isNullOrEmpty()
            //}
            AnimatedVisibility(visible = expanded) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    warrantySearchViewModel.imei_unique.filter {
                        it.contains(searchBox, ignoreCase = true)
                    }.forEach { imeiItem ->
                        DropdownMenuItem(onClick = {
                            selectedImei = imeiItem
                            expanded = !expanded
                            //Updates searchbox with the company selected after it is selected
                            searchBox = selectedImei ?: ""
                            // Trigger the Firebase query when a company is selected
                            warrantySearchViewModel.queryEntryByImei(selectedImei)

                            //Invoke the onImeiSelected
                            onImeiSelected.invoke(imeiItem!!)
                        }, text = {
                            Text(text = imeiItem, modifier = Modifier.padding(16.dp))
                        })
                    }
                }
            }
        } else if (category == "Product Model") {
            searchBox() { searchChange ->
                searchBox = searchChange
            }
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
                        it.productModel.contains(searchBox, ignoreCase = true)
                    }.forEach { company ->
                        DropdownMenuItem(onClick = {
                            selectedCompany = company
                            expanded = false
                            //Updates searchbox with the company selected after it is selected
                            searchBox = selectedCompany?.productModel ?: ""
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
    }
/*
        else if (category == "Warranty End Date") {
            datepicker()
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
                        it.warrantyEndDate.contains(dateBox.text, ignoreCase = true)
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
*/
        /*
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
         */
        }



@Composable
fun searchBox(searchTextChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf( "")}

    Column(modifier = Modifier
        .fillMaxWidth(),
        ){
        SmallTextComponent(text = "Type to Search")
        // Search box
        TextField(
            value = searchText,
            onValueChange = {
                searchText = it
                // You can filter the list here based on the search text
            },
            modifier = Modifier,
            label = { Text("Search") },

            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    modifier = Modifier.clickable {
                        expanded = !expanded
                        searchTextChange(searchText)
                    },

                )
            },
            trailingIcon = {
                val currentSearchText by rememberUpdatedState(searchText)
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = "Toggle Dropdown",
                    modifier = Modifier.clickable {
                        if (currentSearchText.isEmpty()) {
                            // Expand if searchText is empty
                            //expanded = !expanded
                        } else {
                            // Clear searchText and then expand
                            searchText = ""
                            //expanded = true
                        }
                    }
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    searchTextChange(searchText)
                }
            )
        )
    }
}
/* Unused Function
@Composable
fun imeiSearch(warrantySearchViewModel: WarrantySearchViewModel) {
    var searchText by remember { mutableStateOf("Enter Imei to search") }
    var isSearchEnabled by remember { mutableStateOf(false) }
    var isSearchValid by remember { mutableStateOf(true) }
    var selectedImei by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    Column {
        // Search text input with a search icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        expanded = !expanded
                    }
            )
            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    // Enable search only if the input is valid (10 characters)
                    isSearchEnabled = it.length > 10
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (isSearchValid) Color.Gray else Color.Red,
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }

        // Dropdown menu for search results
        AnimatedVisibility(visible = expanded && isSearchEnabled) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Check if the input is valid
                if (isSearchValid) {
                    // Search through the list and show results
                    val itemsToDisplay = warrantySearchViewModel.imei_unique
                        .map { Company(imeiNo = it) }
                        .distinctBy { it.imeiNo }
                        .filter {
                            it.imeiNo.toString().contains(searchText, ignoreCase = true)
                        }
                    itemsToDisplay.forEach { company ->
                        DropdownMenuItem(
                            onClick = {
                                selectedImei = company.imeiNo.toString()
                                expanded = false
                                // Update search text with the selected IMEI
                                searchText = selectedImei ?: ""
                                // Perform additional actions with the selected IMEI if needed
                                // ...
                            },
                            text = { Text(text = company.imeiNo.toString(), modifier = Modifier.padding(16.dp)) }
                        )
                    }

                    // Show "No Match" if there are no matches
                    if (itemsToDisplay.isEmpty()) {
                        DropdownMenuItem(
                            onClick = {
                                expanded = !expanded
                            },
                            text = { Text("No Match", modifier = Modifier.padding(16.dp)) }
                        )
                    }
                } else {
                    // If not valid, show an error message
                    DropdownMenuItem(
                        onClick = {
                            expanded = !expanded
                        },
                        text = { Text("Enter a valid IMEI", modifier = Modifier.padding(16.dp)) }
                    )
                }
            }
        }
    }
}
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicesList(devices: List<Company>,
                warrantySearchViewModel: WarrantySearchViewModel,
                selectedCategory: String,
                selectedCompany: Company?,
                selectedImei: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier= Modifier.height(10.dp))
        // Updates the UI State after selectedImei has change when clicked
        val imeiSearchDeviceResult by warrantySearchViewModel.imeisearchdevice.observeAsState()
        when (selectedCategory) {
            "IMEI Number" -> {
                imeiSearchDeviceResult?.let { queryResult ->
                    if (selectedImei != null) {
                        // Call ShowImeiDetails only when selectedImei is not null
                        ShowImeiDetails(queryResult)
                        Log.d("WITHIN DISPLAYLIST QUERYRESULT", "QUERYRESULTOUTPUT $queryResult")
                    }
                }
            }
            "Company" -> {
                // Handle "Company" category
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
                    /* Remove the all device details
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
                    */
                }
            }
            "Warranty End Date" -> {
                // Handle "Warranty End Date" category
                // Display relevant information or invoke necessary functions
            }
            "Warranty Start Date" -> {
                // Handle "Warranty Start Date" category
                // Display relevant information or invoke necessary functions
            }
            "Product Model" -> {
                // Handle "Product Model" category
                // Display relevant information or invoke necessary functions
            }
            else -> {
                // Handle other categories if needed
            }
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

@Composable
fun ShowImeiDetails(imeiDetails: List<Company>) {
    Column {
        imeiDetails.forEach { company ->
            CompanyDetailsCard(company = company)
            Log.d("IMEIDETAILS IN SHOWIMEIDETAILS", "${company}")
        }
    }
}

@Composable
fun CompanyDetailsCard(company: Company) {
    // You can customize the UI for each company's details here
    // For example:
    SmallTextComponent("Customer: ${company.customer}")
    SmallTextComponent("Extended Warranty Date: ${company.extendedWarrantyDate}")
    SmallTextComponent("IMEI No: ${company.imeiNo}")
    SmallTextComponent("Product Model: ${company.productModel}")
    SmallTextComponent("Warranty End Date: ${company.warrantyEndDate}")
}


@Preview
@Composable
fun DefaultPreviewOfWarrantyScreen() {
    WarrantyScreen()
}