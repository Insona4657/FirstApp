package com.example.applogin.screens

import android.app.DownloadManager
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.ButtonComponent
import com.example.applogin.components.HeadingTextComponent
import com.example.applogin.components.MyTextFieldComponent
import com.example.applogin.data.Company
import com.example.applogin.data.HomeViewModel
import com.example.applogin.data.SignupViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import com.google.firebase.initialize
import kotlinx.coroutines.tasks.await
import androidx.compose.material3.Text as Text



@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel(), signupViewModel: SignupViewModel = viewModel()){
    val companies by homeViewModel.companies.observeAsState(emptyList())
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HeadingTextComponent(stringResource(R.string.home))
            Spacer(modifier = Modifier.height(20.dp))
            CompanyList(companies)
            Spacer(modifier = Modifier.height(20.dp))

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
fun CompanyList(companies: List<Company>) {
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
                companies.filter {
                    it.customer.contains(searchText.text, ignoreCase = true)
                }.forEach { company ->
                    DropdownMenuItem(onClick = {
                        selectedCompany = company
                        expanded = false
                        // Do not update searchText here
                    }, text = {
                        Text(text = company.customer,
                            modifier = Modifier.padding(16.dp))
                    })
                }
            }
        }
    }
}
@Preview
@Composable
fun DefaultPreviewOfHomeScreen() {
    HomeScreen()
}