package com.example.applogin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.applogin.app.SyndesApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SyndesApp()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SyndesApp()
}