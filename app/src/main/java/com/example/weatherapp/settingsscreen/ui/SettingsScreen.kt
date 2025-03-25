package com.example.weatherapp.settingsscreen.ui

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
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B0C2A))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SettingOption(title = "Language", options = listOf("Arabic", "English", "Default"))
        SettingOption(title = "Temp Unit", options = listOf("Celsius °C", "Kelvin K", "Fahrenheit °F"))
        SettingOption(title = "Location", options = listOf("Gps", "Map"))
        SettingOption(title = "Wind Speed Unit", options = listOf("meter/sec", "mile/hour"))
    }
}

@Composable
fun SettingOption(title: String, options: List<String>) {
    var selectedOption by remember { mutableStateOf(options.first()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF2193b0))
            .padding(16.dp)
    ) {
        Text(text = title, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            mainAxisSpacing = 16.dp,
            crossAxisSpacing = 8.dp
        ) {
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    RadioButton(
                        selected = (option == selectedOption),
                        onClick = { selectedOption = option },
                        colors = RadioButtonDefaults.colors(selectedColor = Color.Cyan)
                    )
                    Text(text = option, fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}