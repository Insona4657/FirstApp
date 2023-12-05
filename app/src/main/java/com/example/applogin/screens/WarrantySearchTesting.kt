package com.example.applogin.screens

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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.HeadingTextComponent
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
//import com.example.applogin.components.NonEditableTextWithIcons
import com.example.applogin.components.NormalTextComponent
//import com.example.applogin.components.dropDownList
//import com.example.applogin.components.listSearchOutput
import com.example.applogin.components.mainAppBar
import com.example.applogin.components.mainbackground
import com.example.applogin.data.Company
import com.example.applogin.data.WarrantyTestingViewModel
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarrantyTestingScreen(warrantyTestingViewModel: WarrantyTestingViewModel = viewModel(), homeViewModel: HomeViewModel = viewModel()) {
    val companies by warrantyTestingViewModel.companies.observeAsState(emptyList())
    //val devices by warrantyTestingViewModel.devices.observeAsState(emptyList())
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedChoice by remember { mutableStateOf<Company?>(null) }
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

            ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .padding(paddingValues),
                //To enable the background function to work
                //color = MaterialTheme.colorScheme.background,
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
                        //mainbackground()
                        Column(modifier = Modifier.fillMaxSize()) {
                            Spacer(modifier = Modifier.height(16.dp))

                            // Text Component to show header Warranty Checker
                            HeadingTextComponent(stringResource(R.string.warranty_checker))
                            Spacer(modifier = Modifier.height(20.dp))
                            NonEditableTextWithIcons(dropDownListCallback={
                                categoryChosen -> selectedCategory = categoryChosen
                            })
                            dropDownList(category= selectedCategory, warrantyTestingViewModel,
                                onChoiceSelected = { choiceselected ->
                                    selectedChoice = choiceselected
                                    // Call processing functions after dropDownList
                                })
                            listSearchOutput(selectedChoice = selectedChoice, warrantyTestingViewModel)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun listSearchOutput(selectedChoice: Company?, warrantyTestingViewModel: WarrantyTestingViewModel){
    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment =  Alignment.CenterHorizontally,
    ) {
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
            text = selectedChoice?.customer ?:"",
            modifier = Modifier,
        )
        LazyColumn(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            item {
                warrantyTestingViewModel.queryModel.value?.let {queryResult ->
                    Log.d("QueryModel", queryResult.toString())
                    ShowResultMap(queryResult.resultMap)
                }
                warrantyTestingViewModel.queryDetailWarranty.value?.let { queryResult ->
                    Log.d("QueryDetailWarranty", queryResult.toString())
                    ShowResultMap(queryResult.resultMap)
                }
                warrantyTestingViewModel.queryDetailExtendedWarranty.value?.let { queryResult ->
                    Log.d("QueryDetailExtendedWarranty", queryResult.toString())
                    ShowResultMap(queryResult.resultMap)
                }
            }
        }
    }
}
@Composable
fun NonEditableTextWithIcons(dropDownListCallback: (String) -> Unit) {
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
                    imageVector = Icons.Default.Cancel,
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
                            dropDownListCallback(selectedChoice)
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
fun dropDownList(category: String, warrantyTestingViewModel: WarrantyTestingViewModel, onChoiceSelected: (Company) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedChoice by remember { mutableStateOf<Company?>(null) } // Default is empty
    var searchText by remember { mutableStateOf(TextFieldValue()) }
    Column {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 4.dp,
                top = 8.dp,
                bottom = 8.dp,
            )
            .clickable { expanded = !expanded },
        horizontalArrangement = Arrangement.Center,
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
            }
            ,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    expanded = !expanded
                }
            )
        )
        Icon(
            imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
            contentDescription = "Toggle Dropdown"
        )
    }
        AnimatedVisibility(visible = expanded) {

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                if (category == "Company") {
                    var companiesToDisplay = warrantyTestingViewModel.company_unique
                        .map { Company(customer = it) }
                        .distinctBy { it.customer }
                    companiesToDisplay.filter {
                        it.customer.contains(searchText.text, ignoreCase = true)
                    }.forEach { company ->
                        DropdownMenuItem(onClick = {
                            selectedChoice = company
                            expanded = false
                            //Updates searchbox with the company selected after it is selected
                            searchText = TextFieldValue(text = selectedChoice?.customer ?: "")

                            // Trigger the Firebase query when a company is selected
                            // not sure what this returns so later on check
                            warrantyTestingViewModel.queryDevicesByCompany(selectedChoice!!.customer)

                            // Invoke the callback with the selected company
                            // not sure what this invokes so need to check
                            onChoiceSelected.invoke(selectedChoice!!)

                        }, text = {
                            Text(text = company.customer, modifier = Modifier.padding(16.dp))
                        })

                    }
                }
                else if (category == "Product Model") {
                    var companiesToDisplay = warrantyTestingViewModel.model_unique
                        .map { Company(productModel = it) }
                        .distinctBy { it.productModel }
                    companiesToDisplay.filter {
                        it.productModel.contains(searchText.text, ignoreCase = true)
                    }.forEach { model ->
                        DropdownMenuItem(onClick = {
                            selectedChoice = model
                            expanded = false
                            //Updates searchbox with the company selected after it is selected
                            searchText = TextFieldValue(text = selectedChoice?.productModel ?: "")

                            // Trigger the Firebase query when a Model is selected
                            // Function to be modified
                            //warrantyTestingViewModel.queryDevicesByCompany(selectedChoice!!.productModel)

                            // Invoke the callback with the selected company
                            onChoiceSelected.invoke(selectedChoice!!)
                        }, text = {
                            Text(text = model.productModel, modifier = Modifier.padding(16.dp))
                        })

                    }
                }
            }
        }
        warrantyTestingViewModel.processModel()
        warrantyTestingViewModel.processWarranty()
        warrantyTestingViewModel.processExtendedWarranty()
    }
}
