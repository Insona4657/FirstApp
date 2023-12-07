package com.example.applogin.screens

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.NormalTextComponent
import com.example.applogin.components.mainAppBar
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import com.example.applogin.components.mainbackground
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceRequestScreen(homeViewModel: HomeViewModel = viewModel()){

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var product by rememberSaveable { mutableStateOf("") }
    var serialNumber by rememberSaveable { mutableStateOf("") }
    var invoiceDate by rememberSaveable { mutableStateOf("") }
    var issueDescription by rememberSaveable { mutableStateOf("") }
    var selectedFile: Uri? by rememberSaveable { mutableStateOf(null) }
    val calendarState = UseCaseState()
    val context = LocalContext.current

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date{ date ->
            Log.d("SelectedDate", "$date")
        })
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
                mainAppBar(toolbarTitle = "Service Request",
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
                        //Form
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background) // Set the background color
                                .border(1.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp)),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Spacer(modifier = Modifier.height(20.dp))
                            NormalTextComponent(introText = "Product Model")
                            TextField(value = product,
                                onValueChange = { product = it},
                                label = { Text("Product")},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            )
                            NormalTextComponent(introText = "Serial Number")
                            TextField(value = serialNumber,
                                onValueChange = { serialNumber = it},
                                label = {Text ("Serial Number ")},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                )
                            NormalTextComponent(introText = "Date")
                            Button(onClick = {
                                calendarState.show()
                            }) {
                                Text(text = "Date Picker")
                            }
                            NormalTextComponent(introText = "Issue Description")
                            TextField(
                                value = issueDescription,
                                onValueChange = { issueDescription = it },
                                label = { Text("Issue Description") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            )
                            NormalTextComponent(introText = "Attachments")
                            // File Upload library to be searched
                            // Submit Button
                            Button(
                                onClick = {
                                    // Create a deep link to open WhatsApp with a pre-composed message
                                    val whatsappLink =
                                        "https://wa.me/whatsappphonenumber?text=" +
                                                "Product: $product%0a" +
                                                "Serial Number: $serialNumber%0a" +
                                                "Invoice Date: $invoiceDate%0a" +
                                                "Issue Description: $issueDescription%0a" +
                                                "Attached File: ${selectedFile?.toString()}"

                                    // Launch the intent to open WhatsApp with the composed link
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(whatsappLink))
                                    startActivity(context, intent, null)
                                },
                                modifier = Modifier
                                    .padding(top = 16.dp)
                            ) {
                                Text("Submit")
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
fun DefaultPreviewServiceRequestScreen() {
    ServiceRequestScreen()
}