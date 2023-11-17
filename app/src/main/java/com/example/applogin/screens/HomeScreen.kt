package com.example.applogin.screens

import android.app.DownloadManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.ButtonComponent
import com.example.applogin.components.HeadingTextComponent
import com.example.applogin.components.MyTextFieldComponent
import com.example.applogin.data.SignupViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import com.google.firebase.initialize

@Composable
fun HomeScreen(signupViewModel: SignupViewModel = viewModel()){
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
            // Include the CompanyDropdown here
            // Include the CompanyDropdown here
            CompanyDropdown()
            ButtonComponent(
                value = stringResource(R.string.logout),
                onButtonClicked = {
                    signupViewModel.logout()
                },
                isEnabled = true)
        }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyDropdown() {
    var selectedCompany = ""
    OutlinedTextField(
        value = selectedCompany,
        onValueChange = {

        },
        label = { Text("Select Company") },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview
@Composable
fun DefaultPreviewOfHomeScreen() {
    HomeScreen()
}