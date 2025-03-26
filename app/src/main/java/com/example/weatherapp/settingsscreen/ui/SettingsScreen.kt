package com.example.weatherapp.settingsscreen.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun SettingsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B0C2A))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SettingOption("Language", listOf("Arabic", "English", "Default")) { selected ->
            Log.d("Settings", "Selected Language: $selected")
        }

        SettingOption("Temp Unit", listOf("Celsius °C", "Kelvin K", "Fahrenheit °F")) { selected ->
            Log.d("Settings", "Selected Temp Unit: $selected")
        }

        SettingOption("Location", listOf("Gps", "Map")) { selected ->
            if (selected == "Map") {
                Log.d("Settings", "User selected Map - Open Map Screen")
                navController.navigate("SelectableMapScreen")
            } else {
                Log.d("Settings", "Using GPS")
            }
        }

        SettingOption("Wind Speed Unit", listOf("meter/sec", "mile/hour")) { selected ->
            Log.d("Settings", "Selected Wind Speed: $selected")
        }
    }
}

@Composable
fun SettingOption(title: String, options: List<String>, onOptionSelected: (String) -> Unit) {
    var selectedOption by remember { mutableStateOf(options.first()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF2193b0))
            .padding(12.dp)
    ) {
        Text(text = title, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)

        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = {
                        selectedOption = option
                        onOptionSelected(option)
                    },
                    colors = RadioButtonDefaults.colors(selectedColor = Color.Cyan)
                )
                Text(text = option, fontSize = 16.sp, color = Color.White, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}
