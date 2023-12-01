package com.example.applogin.screens

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.applogin.components.navigationIcon
import com.example.applogin.data.Company
import com.example.applogin.data.WarrantySearchViewModel
import com.example.applogin.data.QueryResults
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.data.signupregistration.SignupViewModel
import kotlinx.coroutines.launch
import androidx.compose.material3.Text as Text


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarrantyScreen(warrantySearchViewModel: WarrantySearchViewModel = viewModel(), signupViewModel: SignupViewModel = viewModel(), homeViewModel: HomeViewModel = viewModel()){
    val companies by warrantySearchViewModel.companies.observeAsState(emptyList())
    val devices by warrantySearchViewModel.devices.observeAsState(emptyList())
    // Observe changes in the queries LiveData
    val queryProduct by warrantySearchViewModel.queryProduct.observeAsState(emptyList())
    val queryWarranty by warrantySearchViewModel.queryWarranty.observeAsState(emptyList())
    val queryExtendedWarranty by warrantySearchViewModel.queryExtendedWarranty.observeAsState(emptyList())


    //val scaffoldState = rememberScaffoldState()
    //val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet{
                Column{
                    NavigationDrawerHeader()
                    NavigationDrawerBody(navigationDrawerItems = homeViewModel.navigationItemsList, onClick = {
                        Log.d(ContentValues.TAG, "Inside NavigationDrawer")
                        Log.d(ContentValues.TAG, "Inside ${it.itemId} ${it.title}")
                    })
                }
            }
        }, drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                mainAppBar(toolbarTitle = stringResource(R.string.home),
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
                .padding(paddingValues)) {
                Column(
                    modifier = Modifier,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .padding(28.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                HeadingTextComponent(stringResource(R.string.warranty_checker))
                                Spacer(modifier = Modifier.height(20.dp))
                                //CompanyList(companies)
                                CompanyList(companies, warrantySearchViewModel)
                                Spacer(modifier = Modifier.height(20.dp))
                                //ShowSummary(homeViewModel)
                                Spacer(modifier = Modifier.height(20.dp))
                                // Display the devices associated with the selected company
                                DevicesList(devices, queryProduct, queryWarranty, queryExtendedWarranty, warrantySearchViewModel)

                            }
                        }
                    }
                    }

                }
            }
        }
    }




@Composable
fun ShowSummary(warrantySearchViewModel: WarrantySearchViewModel) {
    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text ="Summary")
        warrantySearchViewModel.queryModel.value?.let { queryResult ->
            ShowResultMap(queryResult.resultMap)
        }
        warrantySearchViewModel.queryDetailWarranty.value?.let { queryResult ->
            ShowResultMap(queryResult.resultMap)
        }
        warrantySearchViewModel.queryDetailExtendedWarranty.value?.let { queryResult ->
            ShowResultMap(queryResult.resultMap)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RestrictedApi")
@Composable
fun CompanyList(companies: List<Company>, warrantySearchViewModel: WarrantySearchViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCompany by remember { mutableStateOf<Company?>(null) }
    var searchText by remember { mutableStateOf(TextFieldValue()) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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
                        contentDescription = "Search Icon"
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
                val companiesToDisplay = warrantySearchViewModel.company_unique
                    .map { Company(customer = it)}
                    .distinctBy { it.customer }
                companiesToDisplay.filter {
                    it.customer.contains(searchText.text, ignoreCase = true)
                }.forEach { company ->
                    DropdownMenuItem(onClick = {
                        selectedCompany = company
                        expanded = false
                        // Trigger the Firebase query when a company is selected
                        warrantySearchViewModel.queryDevicesByCompany(selectedCompany!!.customer)

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
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicesList(devices: List<Company>,
                queryProduct:List<QueryResults>,
                queryWarranty:List<QueryResults>,
                queryExtendedWarranty:List<QueryResults>,
                warrantySearchViewModel: WarrantySearchViewModel) {


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