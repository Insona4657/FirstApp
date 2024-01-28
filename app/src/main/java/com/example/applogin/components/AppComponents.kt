package com.example.applogin.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applogin.MyFirebaseMessagingService
import com.example.applogin.R
import com.example.applogin.data.NavigationItem
import com.example.applogin.data.home.HomeViewModel
import com.example.applogin.data.signupregistration.SignupUIEvent
import com.example.applogin.data.signupregistration.SignupViewModel
import com.example.applogin.ui.theme.Shapes
import com.google.firebase.auth.FirebaseAuth
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeParseException
import java.util.Locale

@Composable
fun productDisplay() {

}
@Composable
fun mainbackground(){
    Box(
    modifier = Modifier
    .fillMaxWidth(),
    contentAlignment = Alignment.Center,

    ) {
        Image(
            painter = painterResource(id = R.drawable.syndes_bg_screen_home),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentScale = ContentScale.Crop,
        )// Apply a faded effect on top of the image
        Modifier.drawBehind {
            drawRect(
                color = Color.Black.copy(alpha = 0.4f), // Adjust the alpha for the desired fade effect
            )
        }
    }
}
@Composable
fun MainPageTopBackground(topimage: Int, middleimage: Int, bottomimage: Int){
    Box(modifier = Modifier.fillMaxSize(),
        ) {
        Column(modifier = Modifier.fillMaxHeight()) {
            Image(
                painter = painterResource(id = topimage),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f),
                contentScale = ContentScale.Crop,
            )
            Image(
                painter = painterResource(id = middleimage),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.9f),
                contentScale = ContentScale.Crop,
            )
            Image(
                painter = painterResource(id = bottomimage),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.08f),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
fun ServiceRequestBackground(topimage: Int, middleimage: Int, bottomimage: Int){
    Column {
        Box(modifier = Modifier.background(Color.White).weight(1f))
        /*
        Image(
            painter = painterResource(id = topimage),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
                .background(Color.White),
            contentScale = ContentScale.Crop,
        )
        Image(
            painter = painterResource(id = middleimage),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .weight(0.8f)
                .background(Color.White),
            contentScale = ContentScale.Crop,
        )

         */
        Image(
            painter = painterResource(id = bottomimage),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.08f),
            contentScale = ContentScale.Crop,
        )
    }
}


@Composable
fun TwoImageBackground(topimage: Int, middleimage: Int){
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp)),
    ) {
        Image(
            painter = painterResource(id = topimage),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )
        Image(
            painter = painterResource(id = middleimage),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun NavigationDrawerHeader(homeViewModel: HomeViewModel){
    var email = homeViewModel.userEmail.value.toString()
    val user = FirebaseAuth.getInstance().currentUser
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(32.dp)
    ){
        if (email != null) {
            NavigationHeader(introText = email)
        } else {
            if (user != null){
                if (email != user.email)
                    email = user.email.toString()
                    NavigationHeader(introText = email)
            }
        }
    }
}

@Composable
fun NavigationDrawerBody(navigationDrawerItems: List<NavigationItem>, onClick: (NavigationItem) -> Unit) {

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(navigationDrawerItems){
            NavigationItemRow(item = it, onClick)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationItemRow(item: NavigationItem, onClick: (NavigationItem) -> Unit,) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke(item)
            }
            .padding(all = 16.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.description,
        )
        Spacer(
            modifier = Modifier
                .width(18.dp)
        )
        Text(text = item.title)
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun mainAppBar(toolbarTitle: String,
               logoutButtonClicked: () -> Unit,
               navigationIconClicked:() -> Unit,
               barcodeIconClicked: () -> Unit,
               notificationIconClicked: () -> Unit
               ) {
    val context = LocalContext.current
    val unreadCount = remember { MyFirebaseMessagingService.countUnreadNotifications(context).toString() }
    TopAppBar(
        title = {
            Text(
                text = toolbarTitle, color = Color.Black
            ) },
        navigationIcon = {
            IconButton(onClick = {
                navigationIconClicked.invoke()
            }) {
            Icon(imageVector = Icons.Filled.Menu,
                contentDescription = stringResource(R.string.menu),
                tint = Color.Black)
            }
        },
        actions = {
            IconButton(onClick = {
                notificationIconClicked()
            }) {
                Box(modifier = Modifier,
                    contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = stringResource(R.string.notification),
                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    )
                    Box(modifier = Modifier.scale(0.65f)
                        .padding(start = 15.dp, bottom = 10.dp)
                        .background(Color(255, 165, 0), CircleShape)
                        .align(Alignment.Center),
                    ) {
                        Text(
                            text = unreadCount,
                            modifier = Modifier
                                .padding(start = 8.dp, end = 8.dp, top = 5.dp, bottom = 3.dp)
                                .scale(1.2f),
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                        )
                    }
                }
            }
            IconButton(onClick = {
                barcodeIconClicked.invoke()
            }) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Scanner",
                    tint = Color.Black
                )
            }

            IconButton(onClick = {
                logoutButtonClicked()
            }) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = stringResource(R.string.logout)
                )
            }
        }
    )
}
@Composable
fun navigationIcon(pageTitle: String, pageIcon: Painter, navigationIconClicked:() -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(5.dp)

    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp) // Adjust the size as needed
                .background(Color.White, shape = RoundedCornerShape(25.dp))
                .border(1.dp, Color.Transparent, shape = RoundedCornerShape(25.dp))
                .clip(RoundedCornerShape(25.dp))
                .clickable { navigationIconClicked.invoke() },

        ) {
            Image(
                painter = pageIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .aspectRatio(1f),
            )
        }
        Text(
            text = pageTitle,
            fontSize = 16.sp, // Adjust the font size as needed
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SmallTextComponent(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp),
        color = colorResource(id = R.color.TextColor),
        style = TextStyle (
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
        ),
        textAlign = TextAlign.Left
    )
}

@Composable
fun ServiceFormTextComponent(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp),
        color = colorResource(id = R.color.TextColor),
        style = TextStyle (
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
        ),
        textAlign = TextAlign.Left
    )
}


@Composable
fun NormalTextComponent(introText: String) {
    Text(
        text = introText,
        modifier = Modifier
            .fillMaxWidth(),
        color = colorResource(id = R.color.TextColor),
        style = TextStyle (
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
        ),
        textAlign = TextAlign.Center,
    )
}

fun HideKeyboard(context: Context, view: View?) {
    val inputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
}
@Composable
fun ServiceFormComponent(introText: String) {
    Text(
        text = introText,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, top = 25.dp, bottom = 25.dp),
        color = colorResource(id = R.color.TextColor),
        style = TextStyle (
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            fontStyle = FontStyle.Normal,
        ),
        textAlign = TextAlign.Left,
    )
}
@Composable
fun LoginNormalTextComponent(introText: String) {
    Text(
        text = introText,
        modifier = Modifier
            .fillMaxWidth(),
        color = Color(255, 165, 0),
        style = TextStyle (
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
        ),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun ResetPasswordNormalTextComponent(introText: String) {
    Text(
        text = introText,
        modifier = Modifier
            .fillMaxWidth(),
        color = Color.White,
        style = TextStyle (
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
        ),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun HeadingTextComponent(introText: String) {
    Text(
        text = introText,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        color = colorResource(id = R.color.TextColor),
        style = TextStyle (
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
        ),
        textAlign = TextAlign.Center,
    )
}
@Composable
fun NavigationHeader(introText: String) {
    Text(
        text = introText,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 15.dp),
        color = colorResource(id = R.color.TextColor),
        style = TextStyle (
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
        ),
        textAlign = TextAlign.Center,
    )
}
@Composable
fun ProductTextComponent(introText: String) {
    Text(
        text = introText,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 25.dp)
            .padding(start = 20.dp),
        color = Color.White,
        style = TextStyle (
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
        ),
        textAlign = TextAlign.Left,
    )
}
@Composable
fun ProductCompanyComponent(introText: String) {
    Text(
        text = introText,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 30.dp)
            .padding(start = 20.dp),
        color = Color.White,
        style = TextStyle (
            fontSize = 20.sp,
            fontStyle = FontStyle.Normal,
        ),
        textAlign = TextAlign.Left,
    )
}

// Function to convert date string format
fun convertDateFormat(inputDate: String): String {
    if (inputDate.isEmpty() or (inputDate == "") or (inputDate == "NaN")) {
        Log.d("Input is empty, or NaN", "Actual Input to check: ${inputDate}")
        return "No Date" // Return empty string if the input is empty
    }
    // Check if inputDate is a number
    val isNumber = inputDate.toDoubleOrNull() != null

    if (isNumber) {
        // Handle the case when inputDate is a number
        Log.d(TAG, "Input Date is a Number: ${isNumber}")
        return "Invalid Date - Input is a number"
    }

    val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

    try {
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date!!)
    } catch (e: DateTimeParseException) {
        e.printStackTrace()
        // Provide more information about the parsing error
        return "Invalid Date - ${e.message}"
    }
}

@Composable
fun ServiceRequestForm(introText: String) {
    Text(
        text = introText,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 30.dp)
            .padding(start = 20.dp),
        color = Color.White,
        style = TextStyle (
            fontSize = 30.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold
        ),
        textAlign = TextAlign.Left,
    )
}

@Composable
fun InboxText(introText: String) {
    Text(
        text = introText,
        modifier = Modifier
            .padding(start = 20.dp,top = 5.dp),
        color = Color.White,
        style = TextStyle (
            fontSize = 30.sp,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold
        ),
        textAlign = TextAlign.Left,
    )
}

@Composable
fun LoginHeadingTextComponent(introText: String) {
    Text(
        text = introText,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        color = Color.White,
        style = TextStyle (
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
        ),
        textAlign = TextAlign.Center,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextFieldComponent(labelValue: String, imageVector: ImageVector, onTextSelected: (String) -> Unit, errorStatus:Boolean=false){
    var textValue = remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.small),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.Primary),
            focusedLabelColor = colorResource(id = R.color.Primary),
            cursorColor = colorResource(id = R.color.Primary)
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(imageVector = imageVector, contentDescription = "Icon")
        },
        isError = !errorStatus
        )
}


@Composable
fun LoginMyTextFieldComponent(labelValue: String, imageVector: ImageVector, onTextSelected: (String) -> Unit, errorStatus:Boolean=false){
    var textValue = remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.small),
        label = { Text(text = labelValue,
            color = Color.White) },
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Color.White,
            focusedBorderColor = Color.White,
            focusedLabelColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(imageVector = imageVector, contentDescription = "Icon", tint = Color.White)
        },
        //isError = !errorStatus
    )
}

@Composable
fun DropdownTextFieldComponent(labelValue: String,
                               imageVector: ImageVector,
                               onTextSelected: (String) -> Unit,
                               onOptionSelected: (String) -> Unit,
                               errorStatus:Boolean=false,
                               signUpViewModel : SignupViewModel, context:Context) {
    var textValue = remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.small),
        label = {
            Text(
                text = labelValue,
                color = Color.White
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Color.White,
            focusedBorderColor = Color.White,
            focusedLabelColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            onTextSelected(it)
            onOptionSelected(it)
            signUpViewModel.onEvent(SignupUIEvent.CompanyNameChanged(it),context)
        },
        leadingIcon = {
            Icon(imageVector = imageVector, contentDescription = "Icon", tint = Color.White)
        },
        //isError = !errorStatus
        keyboardActions = KeyboardActions(
            onNext = {
                // Updates the companyNames Variable by calling this function when next button is clicked
                signUpViewModel.filterCompanyNames(textValue.value)
                expanded = !expanded
            }
        )

    )

    AnimatedVisibility(visible = expanded) {
        // Access the value of filteredCompanyNames
        val filteredCompanies = signUpViewModel.filteredCompanyNames.value
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = !expanded
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            filteredCompanies.forEach { companyName ->
                DropdownMenuItem(
                    onClick = {
                        textValue.value = companyName
                        expanded = !expanded
                        onTextSelected(companyName)
                        onOptionSelected(companyName)
                    },
                    text = {
                        Text(companyName)
                    }
                )
            }
        }
    }
}

@Composable
fun ResetPasswordTextFieldComponent(
    labelValue: String,
    imageVector: ImageVector,
    onTextSelected: (String) -> Unit,
){
    var textValue = remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.small),
        label = { Text(text = labelValue,
            color = Color.White) },
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Color.White,
            focusedBorderColor = Color.White,
            focusedLabelColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
        ),
        singleLine = true,
        maxLines = 1,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(imageVector = imageVector, contentDescription = "Icon", tint = Color.White)
        }
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextFieldComponent(labelValue: String, imageVector: ImageVector, onTextSelected: (String) -> Unit, errorStatus:Boolean= false) {
    val localFocusManager = LocalFocusManager.current
    var password = remember {
        mutableStateOf("")
    }
    val passwordVisible = remember {
        mutableStateOf(false)
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.small),
        label = { Text(modifier = Modifier,
            text = labelValue,
            color = Color.White) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            focusedLabelColor = Color.White,
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        singleLine = true,
        maxLines = 1,
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        value = password.value,
        onValueChange = {
            password.value = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(imageVector = imageVector, contentDescription = "Icon", tint = Color.White)
        },
        trailingIcon = {
            val iconImage = if(passwordVisible.value){
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            var description = if(passwordVisible.value){
                stringResource(R.string.hide_password)
            } else {
                stringResource(R.string.show_password)
            }
            IconButton(onClick = { passwordVisible.value = !passwordVisible.value}) {
                Icon(imageVector = iconImage, contentDescription = null, tint = Color.White)
            }
        },
        visualTransformation = if(passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        //isError = !errorStatus
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetEmailPasswordTextFieldComponent(labelValue: String, imageVector: ImageVector, onTextSelected: (String) -> Unit, errorStatus:Boolean= false) {
    val localFocusManager = LocalFocusManager.current
    var password = remember {
        mutableStateOf("")
    }
    val passwordVisible = remember {
        mutableStateOf(false)
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.small),
        label = { Text(modifier = Modifier,
            text = labelValue,
            color = Color.Black
        ) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            focusedLabelColor = Color.Black,
            cursorColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            unfocusedBorderColor = Color.Black,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        singleLine = true,
        maxLines = 1,
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        value = password.value,
        onValueChange = {
            password.value = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(imageVector = imageVector, contentDescription = "Icon", tint = Color.White)
        },
        trailingIcon = {
            val iconImage = if(passwordVisible.value){
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            var description = if(passwordVisible.value){
                stringResource(R.string.hide_password)
            } else {
                stringResource(R.string.show_password)
            }
            IconButton(onClick = { passwordVisible.value = !passwordVisible.value}) {
                Icon(imageVector = iconImage, contentDescription = null, tint = Color.Black)
            }
        },
        visualTransformation = if(passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        //isError = !errorStatus
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordFirstTime(labelValue: String, imageVector: ImageVector, onTextSelected: (String) -> Unit, errorStatus:Boolean= false) {
    val localFocusManager = LocalFocusManager.current
    var password = remember {
        mutableStateOf("")
    }
    val passwordVisible = remember {
        mutableStateOf(false)
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(Shapes.small),
        label = { Text(modifier = Modifier,
            text = labelValue,
            color = Color.Gray,
        ) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            focusedLabelColor = Color.Black,
            cursorColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            unfocusedBorderColor = Color.LightGray,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        singleLine = true,
        maxLines = 1,
        keyboardActions = KeyboardActions {
            localFocusManager.clearFocus()
        },
        value = password.value,
        onValueChange = {
            password.value = it
            onTextSelected(it)
        },
        /*
        leadingIcon = {
            Icon(imageVector = imageVector, contentDescription = "Icon", tint = Color.Black)
        },

         */
        trailingIcon = {
            val iconImage = if(passwordVisible.value){
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            var description = if(passwordVisible.value){
                stringResource(R.string.hide_password)
            } else {
                stringResource(R.string.show_password)
            }
            IconButton(onClick = { passwordVisible.value = !passwordVisible.value}) {
                Icon(imageVector = iconImage, contentDescription = null, tint = Color.LightGray)
            }
        },
        visualTransformation = if(passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        //isError = !errorStatus
    )
}
/*
@Composable
fun CheckboxComponent(value: String, onTextSelected :(String) -> Unit, onCheckedChange :(Boolean) -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(56.dp)
        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val checkedState = remember {
            mutableStateOf(false)
        }
        Checkbox(checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = !checkedState.value
                onCheckedChange.invoke(it)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary, // Customize the checked color
                uncheckedColor = Color.White, // Set the unchecked color to the primary color
                checkmarkColor = Color.White, // Customize the checkmark color if needed
            ),
        )
        ClickableTextComponent(value = value, onTextSelected)
    }
}
 */

@Composable
fun ClickableTextComponent(value: String, onTextSelected :(String) -> Unit) {
    val initialText = "By continuing you accept our "
    val privacyPolicyText = "Privacy Policy"
    val andText = " and "
    val termsAndConditions = "Terms of Use"
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.White)) {
            pushStringAnnotation(tag = privacyPolicyText, annotation = privacyPolicyText)
            append(initialText)
        }
        withStyle(style = SpanStyle(color = Color.White, textDecoration = TextDecoration.Underline)) {
            pushStringAnnotation(tag = privacyPolicyText, annotation = privacyPolicyText)
            append(privacyPolicyText)
        }
        withStyle(style = SpanStyle(color = Color.White)) {
            pushStringAnnotation(tag = privacyPolicyText, annotation = privacyPolicyText)
            append(andText)
        }
        withStyle(style = SpanStyle(color = Color.White, textDecoration = TextDecoration.Underline)) {
            pushStringAnnotation(tag = privacyPolicyText, annotation = privacyPolicyText)
            append(termsAndConditions)
        }
    }
        ClickableText(text = annotatedString, onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableTextComponent", "{$span.item}")

                    if(span.item == termsAndConditions || (span.item == privacyPolicyText))
                        onTextSelected(span.item)
                }
        })
}

@Composable
fun ButtonComponent(value: String, onButtonClicked : () -> Unit, isEnabled : Boolean = false) {
    Button(
        onClick = {
            onButtonClicked.invoke()
                  },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 80.dp, end = 80.dp)
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(50.dp),
        enabled = isEnabled
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .background(
                color = Color(255, 165, 0),
                shape = RoundedCornerShape(50.dp)
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White)
        }
    }
}
@Composable
fun LoginButton(value: String, onButtonClicked : () -> Unit) {
    Button(
        onClick = {
            onButtonClicked.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 80.dp, end = 80.dp)
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(50.dp),
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .background(
                color = Color(255, 165, 0),
                shape = RoundedCornerShape(50.dp)
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White)
        }
    }
}

@Composable
fun CancelButtonIcon(value: ImageVector, onButtonClicked : () -> Unit, isEnabled : Boolean = false) {
    Button(
        onClick = {
            onButtonClicked.invoke()
        },
        modifier = Modifier
            .heightIn(48.dp)
            .scale(0.9f),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(50.dp),
        enabled = isEnabled
    ){
        Box(modifier = Modifier
            .heightIn(48.dp)
            .padding(start = 20.dp, end = 20.dp)
            .background(
                color = Color(255, 165, 0),
                shape = RoundedCornerShape(50.dp)
            ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = value,
                contentDescription = "Cancel Icon",
                tint = Color.Black
            )
        }
    }
}
@Composable
fun ResetPasswordButtonComponent(value: String, onButtonClicked : () -> Unit, isEnabled : Boolean = false) {
    Button(
        onClick = {
            onButtonClicked.invoke()
        },
        modifier = Modifier
            .heightIn(48.dp)
            .scale(0.9f),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(50.dp),
        enabled = isEnabled
    ){
        Box(modifier = Modifier
            .heightIn(48.dp)
            .padding(start = 20.dp, end = 20.dp)
            .background(
                color = Color(255, 165, 0),
                shape = RoundedCornerShape(50.dp)
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black)
        }
    }
}

@Composable
fun InitialResetButton(value: String, onButtonClicked : () -> Unit, isEnabled : Boolean = false) {
    Button(
        onClick = {
            onButtonClicked.invoke()
        },
        modifier = Modifier
            .heightIn(48.dp)
            .fillMaxWidth(),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(50.dp),
        enabled = isEnabled
    ){
        Box(modifier = Modifier
            .heightIn(48.dp)
            .fillMaxWidth()
            .background(
                color = Color(255, 165, 0),
                shape = RoundedCornerShape(50.dp)
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                fontStyle = FontStyle.Normal)
        }
    }
}
@Composable
fun DividerTextComponent() {
    Row(modifier = Modifier
        .padding(
            start = 80.dp,
            end = 80.dp
        )
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            color = Color.White)
        Text(modifier = Modifier.padding(8.dp),
            text = "or", fontSize = 20.sp, color = Color.White)
        Divider(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            color = Color.White)
    }
}

@Composable
fun ClickableLoginTextComponent(onTextSelected :(String) -> Unit) {
    val initialText = "Already have an Account ? "
    val loginText = "Login Here"
    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Color.White, textDecoration = TextDecoration.Underline)) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }
    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,
        ),
        text = annotatedString,
        onClick = { offset ->
        annotatedString.getStringAnnotations(offset, offset)
            .firstOrNull()?.also { span ->
                Log.d("ClickableTextComponent", "{$span.item}")

                if(span.item == loginText)
                    onTextSelected(span.item)
            }
    })
}


@Composable
fun ClickableBackToHomeScreen(onTextSelected :(String) -> Unit) {
    val loginText = "Back to Home Screen"
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.White, textDecoration = TextDecoration.Underline)) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }
    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,
        ),
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableTextComponent", "{$span.item}")

                    if(span.item == loginText)
                        onTextSelected(span.item)
                }
        })
}

@Composable
fun ClickableRegistrationPage(onTextSelected :(String) -> Unit) {
    val initialText = "Don't Have an Account ? "
    val loginText = "Register Here"
    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = Color.White, textDecoration = TextDecoration.Underline)) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }
    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,
        ),
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableTextComponent", "{$span.item}")

                    if(span.item == loginText)
                        onTextSelected(span.item)
                }
        })
}

@Composable
fun ClickableForgetPasswordComponent(onTextSelected :(String) -> Unit) {
    val forgetPasswordText = "Forgot your password?"
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.White, textDecoration = TextDecoration.Underline)) {
            pushStringAnnotation(tag = forgetPasswordText, annotation = forgetPasswordText)
            append(forgetPasswordText)
        }
    }
    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,
        ),
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableTextComponent", "{$span.item}")
                    if(span.item == forgetPasswordText)
                        onTextSelected(span.item)
                }
        })
}

@Composable
fun ToLoginClickableTextComponent(onTextSelected :(String) -> Unit) {
    val loginText = "Back to Login"
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.White, textDecoration = TextDecoration.Underline)) {
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }
    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,
            color = Color.White
        ),
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableTextComponent", "{$span.item}")

                    if(span.item == loginText)
                        onTextSelected(span.item)
                }
        })
}