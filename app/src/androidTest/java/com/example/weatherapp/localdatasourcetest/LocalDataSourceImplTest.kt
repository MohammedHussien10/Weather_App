package com.example.weatherapp.localdatasourcetest

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherapp.data.local.LocalDataSourceImpl
import com.example.weatherapp.data.local.WeatherDao
import com.example.weatherapp.data.local.WeatherDatabase
import com.example.weatherapp.data.local.models.WeatherDetails
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class LocalDataSourceImplTest {

    private lateinit var localDataSource: LocalDataSourceImpl
    private lateinit var dao: WeatherDao
    private lateinit var database: WeatherDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDatabase::class.java
        ).build()

        dao = database.weatherDao()
        localDataSource = LocalDataSourceImpl.getInstance(dao)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertWeather() = runTest {
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
        localDataSource.insertWeather(weatherDetails)

        // then
        val result = localDataSource.getWeatherByCity("London", 1).first()
        assertNotNull(result)
        assertThat(result.name, `is`("London"))
        assertThat(result.isFav, `is`(true))
    }
}
