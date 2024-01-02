package com.example.applogin.screens

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.MyFirebaseMessagingService
import com.example.applogin.R
import com.example.applogin.components.MainPageTopBackground
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.ProductCompanyComponent
import com.example.applogin.components.ProductTextComponent
import com.example.applogin.components.mainAppBar
import com.example.applogin.data.Company
import com.example.applogin.data.NewWarrantySearchViewModel
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.launch
import java.time.LocalDate
import androidx.compose.material3.Text as Text


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarrantyScreen(newWarrantySearchViewModel: NewWarrantySearchViewModel = viewModel(),
                   homeViewModel: HomeViewModel = viewModel(),
                   context: Context = LocalContext.current){
    //val devices by warrantySearchViewModel.devices.observeAsState(emptyList())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedCompany by remember { mutableStateOf<Company?>(null) }
    var selectedCategory by remember { mutableStateOf ("")}
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var selectedModel by remember { mutableStateOf<String?>(null) }

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
                        //requestCameraAndStartScanner()
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
                //To enable the background function to work
                color = MaterialTheme.colorScheme.background,
                ) {
                Column(
                    modifier = Modifier,
                ) {
                    Surface(
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        //Background function
                        MainPageTopBackground(
                            topimage =R.drawable.product_category_banner,
                            middleimage = R.drawable.middle_background,
                            bottomimage = R.drawable.bottom_background)
                        Column(modifier = Modifier.fillMaxSize()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            ProductCompanyComponent(introText = "Company")
                            ProductTextComponent(introText = "Warranty Checker")
                            newWarrantySearchViewModel.checkCompanyName()
                            // Text Component to show header Warranty Checker
                            //HeadingTextComponent(stringResource(R.string.warranty_checker))
                            //Spacer(modifier = Modifier.height(20.dp))
                            categoryList(chosenItem={
                                    categoryChosen -> selectedCategory = categoryChosen })
                            //Function to display Company List
                            CompanyList(
                                newWarrantySearchViewModel,
                                onCompanySelected = { company ->
                                        selectedCompany = company
                                },
                                category = selectedCategory,
                                onDateSelected = { date ->
                                    selectedDate = date
                                },
                                onModelSelected = { model ->
                                    selectedModel = model
                                })
                            Spacer(modifier = Modifier.height(20.dp))
                            Spacer(modifier = Modifier.height(20.dp))
                            // Display the devices associated with the selected company
                            DevicesList(
                                newWarrantySearchViewModel,
                                selectedCategory = selectedCategory,
                                selectedCompany = selectedCompany,
                                selectedDate = selectedDate,
                                selectedModel = selectedModel)

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
            .padding(start = 16.dp, end = 16.dp, top = 5.dp, bottom = 5.dp)
            .border(1.dp, Color.Transparent, shape = RoundedCornerShape(15.dp)) // Add border
            .clip(RoundedCornerShape(10.dp))
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
                text = selectedChoice.ifEmpty { "Select Category to Search" },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                color = Color.White // Customize text color as needed
            )
            if (!expanded) {
                // Trailing Icon
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Trailing Icon",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
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
                        },
                    tint = Color.White
                )
            }
            Box(modifier = Modifier.padding(10.dp),
                contentAlignment = Alignment.Center) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    val choices = listOf(
                        //"Company",
                        "IMEI Number",
                        "Product Model",
                        "Warranty Date",
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
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun datepicker(onDateSelected: (LocalDate) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedDate = remember { mutableStateOf<LocalDate?>(LocalDate.now()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        TextField(
            value = selectedDate.value.toString() ?: "",
            onValueChange = {},
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(254, 175, 8),
                unfocusedContainerColor = Color(254, 175, 8),
                disabledContainerColor = Color(254, 175, 8),
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
            ),
            label = {
                Text(
                    "Selected Date",
                    color = Color.White,
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        expanded = true // Set the expanded state to true to show the dialog
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "Select Date",
                        tint = Color.White,
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
                .clip(RoundedCornerShape(10.dp))
        )

        // Show the calendar dialog when expanded is true
        if (expanded) {
            CalendarDialog(
                state = rememberUseCaseState(visible = expanded),
                config = CalendarConfig(
                    monthSelection = true,
                    yearSelection = true
                ),
                selection = CalendarSelection.Date(
                    selectedDate = selectedDate.value
                ) { newDate ->
                    selectedDate.value = newDate
                    onDateSelected(newDate)
                    expanded = !expanded
                },
            )
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun CompanyList(newWarrantySearchViewModel: NewWarrantySearchViewModel,
                // Callback to pass selected company
                onCompanySelected: (Company) -> Unit,
                category: String,
                onDateSelected: (LocalDate) -> Unit,
                onModelSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCompany by remember { mutableStateOf<Company?>(null) }
    var searchBox by remember {mutableStateOf ("")}
    var searchModel by remember { mutableStateOf ("") }
    var imeiList by remember { mutableStateOf<List<String>>(emptyList()) }
    var modelList by remember { mutableStateOf<List<String>>(emptyList()) }
    // Variable to track if the block has been executed
    var initialBlockExecuted by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { expanded = !expanded },
        ) {
        if (category == "IMEI Number") {
            searchBox() { searchChange ->
                searchBox = searchChange
                imeiList = newWarrantySearchViewModel.getuniqueImei(searchBox)
                expanded = !expanded
            }
            AnimatedVisibility(visible = expanded) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    imeiList.forEach { imeiItem ->
                        DropdownMenuItem(onClick = {
                            expanded = !expanded
                            //Updates searchbox with the company selected after it is selected
                            searchBox = imeiItem
                            //Search for the item in a function and return Company Details
                            selectedCompany = newWarrantySearchViewModel.checkImeiNumber(imeiItem)
                            // Invoke the callback with the selected company
                            selectedCompany?.let { onCompanySelected(it) }
                        }, text = {
                            Text(text = imeiItem, modifier = Modifier.padding(16.dp))
                        })
                    }
                }
            }
        } else if (category == "Product Model") {
            modelList = newWarrantySearchViewModel.getUniqueModel()

            searchBox() { searchChange ->
                searchModel = searchChange
                expanded = !expanded
            }
            if (!initialBlockExecuted) {
                expanded = !expanded
                initialBlockExecuted = true
            }

            AnimatedVisibility(visible = expanded) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Filter the modelList based on the searchModel string
                    val filteredModelList = modelList.filter {
                        it.contains(searchModel, ignoreCase = true)
                    }
                    if (filteredModelList.isEmpty()) {
                        // Show a message when no matching models are found
                        Text("No matching models")
                    } else {
                        // Display filtered models in the DropdownMenu
                        filteredModelList.forEach { modelName ->
                            DropdownMenuItem(onClick = {
                                searchModel = modelName
                                searchBox = modelName
                                onModelSelected(modelName)
                                expanded = false
                            }, text = {
                                Text(text = modelName, modifier = Modifier.padding(16.dp))
                            })
                        }
                    }
                    /*
                    modelList.forEach { modelName ->
                        DropdownMenuItem(onClick = {
                            searchModel = modelName
                            onModelSelected(modelName)
                            expanded = !expanded
                        }, text = {
                            Text(text = modelName, modifier = Modifier.padding(16.dp))
                        })
                    }
                     */
                }
            }
        } else if (category == "Warranty Date") {
            datepicker() { selectedDate ->
                onDateSelected(selectedDate)
            }

            }
        }
    }

@Composable
fun searchBox(searchTextChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf( "")}

    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { expanded = !expanded }
        .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)
        .border(1.dp, Color.Transparent, shape = RoundedCornerShape(15.dp)) // Add border
        .clip(RoundedCornerShape(10.dp))
        .background(Color(254, 175, 8)), // Set background color,
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            // Search box
            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    // You can filter the list here based on the search text
                },
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(254, 175, 8),
                    unfocusedContainerColor = Color(254, 175, 8),
                    disabledContainerColor = Color(254, 175, 8),
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedIndicatorColor = Color(254, 175, 8),
                    focusedIndicatorColor = Color(254, 175, 8),

                ),
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search",color = Color.White) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        modifier = Modifier
                            .clickable {
                            expanded = !expanded
                            searchTextChange(searchText)
                        },
                        tint = Color.White,
                        )
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Toggle Dropdown",
                        modifier = Modifier
                            .padding(end = 10.dp),
                        tint = Color.White
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        expanded = !expanded
                        searchTextChange(searchText)
                    }
                )
            )
        }
    }
}

@Composable
fun DevicesList(newWarrantySearchViewModel: NewWarrantySearchViewModel,
                selectedCategory: String,
                selectedCompany: Company?,
                selectedDate: LocalDate?,
                selectedModel: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier= Modifier.height(10.dp))
        when (selectedCategory) {
            "IMEI Number" -> {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 40.dp),
                    verticalArrangement = Arrangement.Center
                ){
                    item{
                        selectedCompany?.let {
                            Text(text = "Name: ${it.customer}")
                            Text(text = "Model: ${it.productModel}")
                            Text(text = "Extended Warranty Date: ${it.extendedWarrantyDate}")
                            Text(text = "Warranty Date: ${it.warrantyEndDate}")
                            Text(text = "Imei No: ${it.imeiNo.toString()}")
                        }
                    }
                }
            }
            "Product Model" -> {
                val summaryMap = selectedModel?.let {
                    newWarrantySearchViewModel.sumDevicesByModel(it)
                } ?: emptyMap()
                // Handle "Company" category
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
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
                    }
                    summaryMap.forEach { (date, modelCountMap) ->
                        item {
                            Text(
                                text = "Date: $date",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                        modelCountMap.forEach { (modelName, count) ->
                            item {
                                Text(
                                    text = "Model: $modelName, Count: $count",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
            "Warranty Date" -> {
                Box(modifier = Modifier
                    .padding(bottom = 30.dp)
                    .fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        val categorizedDevices =
                            selectedDate?.let {
                                newWarrantySearchViewModel.categorizeDevicesByWarrantyStatus(
                                    it
                                )
                            }
                        categorizedDevices?.forEach { (status, deviceMap) ->
                            // Display the status (e.g., "Devices with Expired Warranty" or "Devices with Active Warranty")
                            Text(
                                text = status,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                textAlign = TextAlign.Center
                            )

                            // Display devices for each model
                            deviceMap.forEach { (model, devices) ->
                                Text(
                                    text = "Name: $model",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    textAlign = TextAlign.Center
                                )
                                // Hash the devices based on unique ExtendedWarrantyDate and count occurrences
                                val devicesByDate = devices.groupingBy { it.extendedWarrantyDate }.eachCount()

                                // Display devices with their occurrences
                                LazyColumn {
                                    items(devicesByDate.entries.toList()) { entry ->
                                        val (extendedWarrantyDate, count) = entry

                                        Text(text = "Warranty Due Date: $extendedWarrantyDate, Count: $count")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
/*
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
*/

@Preview
@Composable
fun DefaultPreviewOfWarrantyScreen() {
    WarrantyScreen()
}