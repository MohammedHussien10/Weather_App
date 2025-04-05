package com.example.weatherapp.weatherdaotest

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.local.WeatherDao
import com.example.weatherapp.data.local.WeatherDatabase
import com.example.weatherapp.data.local.models.WeatherDetails
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DaoTest {

    lateinit var dao: WeatherDao
    lateinit var database: WeatherDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()

        dao = database.weatherDao()
    }

    @After
    fun tearDown() = database.close()

    @Test
    fun testInsertAndDeleteWeather() = runTest {
        // given
        val weatherDetails = WeatherDetails(
            id = 1,
            isFav = true,
            lon = -0.1278,
            lat = 51.5074,
            name = "London",
            temp = 22.5,
            feels_Like_Human = 20.0,
            temp_Min = 18.0,
            temp_Max = 25.0,
            pressure = 1013,
            humidity = 80,
            windSpeed = 5.0,
            windDeg = 270,
            country = "GB",
            sunrise = 1618282134L,
            sunset = 1618333901L,
            dt = System.currentTimeMillis() / 1000
        )

        // when
        dao.insertWeather(weatherDetails)

        val weatherFromDb = dao.getWeatherByCity("London", 1).first()

        // then
        assertNotNull(weatherFromDb)
        assertThat(weatherFromDb.isFav, `is`(true))
        // now delete
        dao.deleteWeatherByCity("London", 51.5074, -0.1278)

        val weatherAfterDelete = dao.getWeatherByCity("London", 1).first()

        // verify the deletion
        assertNull(weatherAfterDelete) // Ensure it's null or empty after deletion
    }
}
