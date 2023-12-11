package com.example.applogin.screens

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.HeadingTextComponent
import com.example.applogin.components.MainPageTopBackground
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.SmallTextComponent
import com.example.applogin.components.mainAppBar
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(homeViewModel: HomeViewModel = viewModel()){

    //val scaffoldState = rememberScaffoldState()
    //val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                mainAppBar(toolbarTitle = "Contact",
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
                    }
                )
            },

            ){ paddingValues ->
            Surface(modifier = Modifier
                .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                MainPageTopBackground(topimage = R.drawable.syndes_bg_screen_home)
                Column(
                    modifier = Modifier,
                ) {
                    Spacer(modifier = Modifier.height(250.dp))
                    HeadingTextComponent(introText = "Contact")
                    SmallTextComponent(text = "Email")
                    SmallTextComponent(text = "Whatsapp")
                    SmallTextComponent(text = "Website")
                }
            }
        }
    }
}
@Preview
@Composable
fun DefaultPreviewContactScreen() {
    ContactScreen()
}