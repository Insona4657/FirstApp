package com.example.applogin.screens

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.util.Log

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.NormalTextComponent
import com.example.applogin.components.mainAppBar
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.content.ContextCompat.startActivity
import com.example.applogin.R
import com.example.applogin.components.MainPageTopBackground
import com.example.applogin.components.ProductCompanyComponent
import com.example.applogin.components.ServiceFormComponent
import com.example.applogin.components.ServiceFormTextComponent
import com.example.applogin.components.ServiceRequestBackground
import com.example.applogin.components.ServiceRequestForm
import com.example.applogin.components.SmallTextComponent
import com.example.applogin.components.mainbackground
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceRequestScreen(homeViewModel: HomeViewModel = viewModel()){

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var product by rememberSaveable { mutableStateOf("") }
    var serialNumber by rememberSaveable { mutableStateOf("") }
    var invoiceDate by rememberSaveable { mutableStateOf("") }
    var issueDescription by rememberSaveable { mutableStateOf("") }
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
                //Background function
                ServiceRequestBackground(
                    topimage = R.drawable.product_category_banner,
                    middleimage = R.drawable.middle_background,
                    bottomimage = R.drawable.bottom_background)
                //Form
                Column {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp, bottom = 30.dp, start = 10.dp, end = 10.dp),
                    ) {
                        ServiceRequestForm(introText = "Service Request Form")

                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(25.dp))
                    ) {
                        ServiceFormComponent(introText = "Product Service Form")
                        Column(modifier = Modifier
                            .fillMaxWidth()
                        ) {
                            ServiceFormTextComponent(text = "Product")
                            ServiceFormTextField("Enter Product Details"){ updatedText ->
                                product = updatedText
                            }
                            ServiceFormTextComponent(text = "Serial Number")
                            ServiceFormTextField("EX:SA6588M75FG"){ updatedText ->
                                serialNumber = updatedText
                            }

                            ServiceFormTextComponent(text = "Invoice Date")
                            serviceformdatepicker(){ selectedDate ->
                                invoiceDate = selectedDate.toString()
                            }
                            ServiceFormTextComponent(text = "Issue Description")
                            ServiceFormTextField("Enter Detail Description"){ updatedText ->
                                issueDescription = updatedText
                            }
                            // Submit button
                            Box(modifier = Modifier
                                .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(
                                    onClick = {
                                        // Create a deep link to open WhatsApp with a pre-composed message
                                        val whatsappLink =
                                            "https://wa.me/601140467496?text=" +
                                                    "Product: $product%0a" +
                                                    "Serial Number: $serialNumber%0a" +
                                                    "Invoice Date: $invoiceDate%0a" +
                                                    "Issue Description: $issueDescription%0a"

                                        // Launch the intent to open WhatsApp with the composed link
                                        val intent =
                                            Intent(Intent.ACTION_VIEW, Uri.parse(whatsappLink))
                                        startActivity(context, intent, null)
                                    },
                                    //modifier = Modifier.background(Color(255, 165, 0))
                                    modifier = Modifier
                                        .background(
                                            color = Color(255, 165, 0),
                                            shape = RoundedCornerShape(50.dp)
                                        ),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(255, 165, 0),
                                        contentColor = Color.White
                                    )
                                    ) {
                                    Text("Submit Form",
                                        color = Color.White,
                                        modifier = Modifier
                                            .background(Color(255, 165, 0)),
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun serviceformdatepicker(onDateSelected: (LocalDate) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedDate = remember { mutableStateOf<LocalDate?>(LocalDate.now()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 25.dp,
                end = 25.dp,
                top = 16.dp,
                bottom = 16.dp)
    ) {
        TextField(
            value = selectedDate.value.toString() ?: "",
            onValueChange = {},
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Color(116, 116, 116),
                focusedContainerColor = Color(234,234,234),
                unfocusedContainerColor = Color(234,234,234),
                disabledContainerColor = Color(234,234,234),
                cursorColor = Color(116, 116, 116),
                focusedTextColor = Color(116, 116, 116),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            label = {
                Text(
                    "Selected Date",
                    color = Color(116, 116, 116),
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
                        tint = Color(116, 116, 116),
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
                .clip(RoundedCornerShape(25.dp))
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

/*
fun servicedatepicker(onDateSelected: (LocalDate) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedDate = remember { mutableStateOf<LocalDate?>(LocalDate.now()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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
                .clip(RoundedCornerShape(15.dp))
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
*/

@Composable
fun ServiceFormTextField(initialValue: String = "",
                         updatedText: (String) -> Unit) {
    var productText by remember { mutableStateOf( "")}
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 10.dp, end = 10.dp, top = 15.dp, bottom = 15.dp)
    ) {
        TextField(
            value = productText,
            onValueChange = {
                productText = it
                updatedText(productText)
                // You can filter the list here based on the search text
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(25.dp)),
            textStyle = LocalTextStyle.current.copy(color = Color(116, 116, 116)), // Adjust text color as needed
            singleLine = true,
            label = { Text(initialValue, color = Color(116, 116, 116)) },
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Color(191, 191, 191),
                focusedContainerColor = Color(234, 234, 234),
                unfocusedContainerColor = Color(234, 234, 234),
                focusedTextColor = Color(191, 191, 191),
                cursorColor = Color(191, 191, 191),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    updatedText(productText)
                }
            )
        )
    }
}

@Preview
@Composable
fun DefaultPreviewServiceRequestScreen() {
    ServiceRequestScreen()
}