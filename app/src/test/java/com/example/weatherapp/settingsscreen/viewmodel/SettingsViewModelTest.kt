package com.example.weatherapp.settingsscreen.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.repository.WeatherRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsViewModelTest{

    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var repository: WeatherRepository

    @Before
    fun setup() {
        // Given
        repository = mockk(relaxed = true)
        settingsViewModel = SettingsViewModel(repository)
    }

    @Test
    fun saveLanguage_callsRepositoryWithCorrectValue(): Unit = runTest {
        // When
        settingsViewModel.saveLanguage("ar")

        // Then
        coVerify { repository.saveLanguage("ar") }
    }

    @Test
    fun saveTempUnit_callsRepositoryWithCorrectValue() = runTest {
        // When
        settingsViewModel.saveTempUnit("Celsius")

        // Then
        coVerify { repository.saveTempUnit("Celsius") }
    }

}