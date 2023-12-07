package com.example.applogin.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.applogin.R
import com.example.applogin.data.NavigationItem
import com.example.applogin.data.login.LoginUIEvent
import com.example.applogin.data.login.LoginViewModel
import com.example.applogin.ui.theme.Shapes

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
fun MainPageTopBackground(topimage: Int){
    Image(
        painter = painterResource(id = topimage),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth(),
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun NavigationDrawerHeader(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(32.dp)
    ){
        HeadingTextComponent(introText = stringResource(R.string.navigation))
    }
}

@Composable
fun NavigationDrawerBody(navigationDrawerItems: List<NavigationItem>, onClick:(NavigationItem) -> Unit) {
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
fun mainAppBar(toolbarTitle: String, logoutButtonClicked : () -> Unit, navigationIconClicked:() -> Unit) {
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
            .padding(5.dp),

    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp) // Adjust the size as needed
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, Color.White, CircleShape)
                .clickable { navigationIconClicked.invoke()},

        ) {
            Image(
                painter = pageIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
            )
        }
        Spacer(modifier = Modifier.height(8.dp)) // Add spacing between Image and Text
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
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.Primary),
            focusedLabelColor = colorResource(id = R.color.Primary),
            cursorColor = colorResource(id = R.color.Primary)
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
            Icon(imageVector = imageVector, contentDescription = "Icon")
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
                Icon(imageVector = iconImage, contentDescription = null)
            }
        },
        visualTransformation = if(passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        isError = !errorStatus
    )
}

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
            })
        ClickableTextComponent(value = value, onTextSelected)
    }
}

@Composable
fun ClickableTextComponent(value: String, onTextSelected :(String) -> Unit) {
    val initialText = "By continuing you accept our "
    val privacyPolicyText = "Privacy Policy"
    val andText = " and "
    val termsAndConditions = "Terms of Use"
    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = colorResource(id = R.color.Primary))) {
            pushStringAnnotation(tag = privacyPolicyText, annotation = privacyPolicyText)
            append(privacyPolicyText)
        }
        append(andText)
        withStyle(style = SpanStyle(color = colorResource(id = R.color.Primary))) {
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
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = RoundedCornerShape(50.dp),
        enabled = isEnabled
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        colorResource(R.color.Secondary),
                        colorResource(id = R.color.Primary)
                    ),
                ),
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
fun DividerTextComponent() {
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            color = colorResource(id = R.color.GrayColor)
        )
        Text(modifier = Modifier.padding(8.dp),
            text = "or", fontSize = 20.sp, color = colorResource(id = R.color.black))
        Divider(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            color = colorResource(id = R.color.GrayColor)
        )
    }
}

@Composable
fun ClickableLoginTextComponent(onTextSelected :(String) -> Unit) {
    val initialText = "Already have an Account ? "
    val loginText = "Login Here"
    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = colorResource(id = R.color.Primary))) {
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
        withStyle(style = SpanStyle(color = colorResource(id = R.color.GrayColor))) {
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
fun ToRegistrationTextComponent(onTextSelected :(String) -> Unit) {
    val initialText = "Don't have an account yet? "
    val loginText = " Register Here"
    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(color = colorResource(id = R.color.Primary))) {
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