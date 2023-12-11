package com.example.applogin.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.applogin.R
import com.example.applogin.components.MainPageTopBackground
import com.example.applogin.components.NavigationDrawerBody
import com.example.applogin.components.NavigationDrawerHeader
import com.example.applogin.components.mainAppBar
import com.example.applogin.components.navigationIcon
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.loginflow.navigation.AppRouter
import com.example.applogin.loginflow.navigation.AppRouter.getScreenForTitle
import com.example.applogin.loginflow.navigation.AppRouter.navigateTo
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransitionScreen(homeViewModel: HomeViewModel = viewModel()){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet{
                Column{
                    NavigationDrawerHeader()
                    NavigationDrawerBody(navigationDrawerItems = homeViewModel.navigationItemsList, onClick = {
                        Log.d(TAG, "Inside NavigationDrawer")
                        Log.d(TAG, "Inside ${it.itemId} ${it.title}")
                        navigateTo(getScreenForTitle(it.title))
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
                    },
                    barcodeIconClicked = {
                        AppRouter.navigateTo(AppRouter.getScreenForTitle("Barcode Scanner"))
                    }
                )
            },

            ){ paddingValues ->
            Surface(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
                color = MaterialTheme.colorScheme.background,
                ) {
                MainPageTopBackground(topimage = R.drawable.syndes_bg_screen_home)
                Column(
                    modifier = Modifier
                        //.fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                        .padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Spacer(modifier = Modifier.height(270.dp))
                    // First Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(4.dp),
                                //.background(Color.White),
                        ) {
                            navigationIcon(
                                stringResource(
                                    R.string.products),
                                    pageIcon = painterResource(id = R.drawable.products_page),
                                    navigationIconClicked = {
                                        Log.d(TAG, "Inside Page Navigation")
                                        Log.d(TAG, "Inside products")
                                        navigateTo(getScreenForTitle("Products Page"))
                                    }
                                )
                        }
                        Box(
                            modifier = Modifier
                                .padding(4.dp),
                        ) {
                            navigationIcon(
                                stringResource(
                                    R.string.services),
                                    pageIcon = painterResource(
                                    id = R.drawable.service_request),
                                navigationIconClicked = {
                                    Log.d(TAG, "Inside Page Navigation")
                                    Log.d(TAG, "Inside services")
                                    navigateTo(getScreenForTitle("Service Request"))
                                })
                        }
                    }

                    // Second Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(
                                    start = 0.dp,
                                    end = 0.dp,
                                    top = 4.dp,
                                    bottom = 4.dp
                                ),
                        ) {
                            navigationIcon(
                                stringResource(R.string.warranty_search),
                                pageIcon = painterResource(
                                    id = R.drawable.warranty_search),
                                navigationIconClicked = {
                                    Log.d(TAG, "Inside Page Navigation")
                                    Log.d(TAG, "Inside warranty_search")
                                    navigateTo(getScreenForTitle("Warranty Search"))
                                })
                        }
                        Box(
                            modifier = Modifier
                                .padding(
                                    start = 0.dp,
                                    end = 16.dp,
                                    top = 4.dp,
                                    bottom = 4.dp
                                )
                        ) {
                            navigationIcon(
                                stringResource(R.string.profile_page),
                                pageIcon = painterResource(
                                    id = R.drawable.profile_page),
                                navigationIconClicked = {
                                    Log.d(TAG, "Inside Page Navigation")
                                    Log.d(TAG, "Inside profile")
                                    navigateTo(getScreenForTitle("Profile Page"))
                                })
                        }
                    }

                    // Third Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(4.dp),
                        ) {
                            navigationIcon(
                                stringResource(R.string.contact),
                                pageIcon = painterResource(
                                    id = R.drawable.contact),
                                navigationIconClicked = {
                                    Log.d(TAG, "Inside Page Navigation")
                                    Log.d(TAG, "Inside contact")
                                    navigateTo(getScreenForTitle("Contact"))
                                })
                        }
                        /*
                        Box(
                            modifier = Modifier
                                .padding(4.dp),
                        ) {

                            navigationIcon(
                                "TO DO",
                                pageIcon = painterResource(id = R.drawable.service_request),
                                navigationIconClicked = {
                                    Log.d(TAG, "Inside Page Navigation")
                                    Log.d(TAG, "Inside todo")
                                    navigateTo(getScreenForTitle("TO DO"))
                                })


                        }
                        */
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreviewTransitionScreen() {
    TransitionScreen()
}