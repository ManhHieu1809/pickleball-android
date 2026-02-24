package com.example.pickleball

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.pickleball.navigation.AppNavigation
import com.example.pickleball.ui.theme.PickleballTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PickleballTheme {
                AppNavigation()
            }
        }
    }
}
