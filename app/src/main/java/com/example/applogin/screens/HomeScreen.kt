package com.example.applogin.screens

import android.annotation.SuppressLint
import android.app.DownloadManager.Query
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.ButtonComponent
import com.example.applogin.components.HeadingTextComponent
import com.example.applogin.data.Company
import com.example.applogin.data.HomeViewModel
import com.example.applogin.data.QueryResults
import com.example.applogin.data.SignupViewModel
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import androidx.compose.material3.Text as Text


@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel(), signupViewModel: SignupViewModel = viewModel()){
    val companies by homeViewModel.companies.observeAsState(emptyList())
    val devices by homeViewModel.devices.observeAsState(emptyList())
    // Observe changes in the queries LiveData
    val queryProduct by homeViewModel.queryProduct.observeAsState(emptyList())
    val queryWarranty by homeViewModel.queryWarranty.observeAsState(emptyList())
    val queryExtendedWarranty by homeViewModel.queryExtendedWarranty.observeAsState(emptyList())

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
            CompanyList(companies, homeViewModel)
            Spacer(modifier = Modifier.height(20.dp))
            ShowSummary(homeViewModel)
            Spacer(modifier = Modifier.height(20.dp))
            // Display the devices associated with the selected company
            DevicesList(devices, queryProduct, queryWarranty, queryExtendedWarranty, homeViewModel)
            Spacer(modifier = Modifier.height(20.dp))

            ButtonComponent(
                value = stringResource(R.string.logout),
                onButtonClicked = {
                    signupViewModel.logout()
                },
                isEnabled = true)

        }
        }
}

@Composable
fun ShowSummary(homeViewModel: HomeViewModel) {
    Column(modifier = Modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text ="Summary")
        homeViewModel.queryModel.value?.let { queryResult ->
            ShowResultMap(queryResult.resultMap)
        }
        homeViewModel.queryDetailWarranty.value?.let { queryResult ->
            ShowResultMap(queryResult.resultMap)
        }
        homeViewModel.queryDetailExtendedWarranty.value?.let { queryResult ->
            ShowResultMap(queryResult.resultMap)
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
fun CompanyList(companies: List<Company>, homeViewModel: HomeViewModel) {
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
            Text(
                text = selectedCompany?.customer ?: "Select a company",
                modifier = Modifier.weight(1f)
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
                val companiesToDisplay = homeViewModel.company_unique.map { Company(customer = it)}
                companiesToDisplay.filter {
                    it.customer.contains(searchText.text, ignoreCase = true)
                }.forEach { company ->
                    DropdownMenuItem(onClick = {
                        selectedCompany = company
                        expanded = false

                        // Trigger the Firebase query when a company is selected
                        homeViewModel.queryDevicesByCompany(selectedCompany!!.customer)

                    }, text = {
                        Text(text = company.customer,
                            modifier = Modifier.padding(16.dp))
                    })

                }
            }
        }
        homeViewModel.processModel()
        homeViewModel.processWarranty()
        homeViewModel.processExtendedWarranty()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicesList(devices: List<Company>,
                queryProduct:List<QueryResults>,
                queryWarranty:List<QueryResults>,
                queryExtendedWarranty:List<QueryResults>,
                homeViewModel: HomeViewModel) {


    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier= Modifier.height(10.dp))
        // Display query results if available
    }
    LazyColumn {
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