//package com.example.weatherapp.data.repository
//
//import android.content.Context
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.example.weatherapp.data.datastore.DataStoreManager
//import com.example.weatherapp.data.local.WeatherDao
//import com.example.weatherapp.data.remote.Clouds
//import com.example.weatherapp.data.remote.Coord
//import com.example.weatherapp.data.remote.Main
//import com.example.weatherapp.data.remote.RemoteDataSourceImpl
//import com.example.weatherapp.data.remote.Sys
//import com.example.weatherapp.data.remote.Weather
//import com.example.weatherapp.data.remote.WeatherApiService
//import com.example.weatherapp.data.remote.WeatherResponse
//import com.example.weatherapp.data.remote.Wind
//import com.example.weatherapp.fakedatastoremanager.FakeDataStoreManager
//import com.example.weatherapp.fakelocaldatasource.FakeLocalDataSource
//import com.example.weatherapp.fakeremotedatasource.FakeRemoteDataSource
//import kotlinx.coroutines.test.runTest
//import org.hamcrest.MatcherAssert.assertThat
//import org.hamcrest.Matchers.`is`
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.Mockito.mock
//import org.mockito.Mockito.`when`
//
//@RunWith(AndroidJUnit4::class)
//class WeatherRepositoryTest{
//
//    private lateinit var fakeDataStoreManager: FakeDataStoreManager
//    private lateinit var fakeRemoteDataSource: FakeRemoteDataSource
//    private lateinit var fakeLocalDataSource: FakeLocalDataSource
//    private lateinit var repository: WeatherRepository
//
//    @Before
//    fun setup() {
//        fakeDataStoreManager = FakeDataStoreManager()
//        fakeRemoteDataSource = FakeRemoteDataSource()
//        repository = WeatherRepository(
//            fakeRemoteDataSource,
//            fakeLocalDataSource,
//            fakeDataStoreManager
//        )
//    }
//
//    @Test
//    fun testSaveLanguage(): Unit = runTest {
//        // Given
//        val language = "en"
//        val fakeDataStoreManager = FakeDataStoreManager()
//        val repository = WeatherRepository.getInstance(
//            mock(Context::class.java),
//            mock(WeatherApiService::class.java),
//            mock(WeatherDao::class.java)
//        )
//
//        // When
//        fakeDataStoreManager.saveLanguage(language)
//
//        // Then
//        assertThat(fakeDataStoreManager.savedLanguage, `is`(language))
//    }
//
//    @Test
//    fun testGetWeatherByCityName() : Unit = runTest {
//        // given
//        val city = "London"
//        val apiKey = "1"
//        val fakeWeatherResponse = WeatherResponse(
//            name = "London",
//            weather = listOf(
//                Weather(
//                    id = 1,
//                    main = "Clear",
//                    description = "Clear sky",
//                    icon = "01d"
//                )
//            ),
//            main = Main(
//                temp = 25.0,
//                pressure = 1013,
//                humidity = 80,
//                feels_Like_Human = 26.0,
//                temp_Min = 20.0,
//                temp_Max = 30.0
//            ),
//            wind = Wind(
//                speed = 5.0, deg = 1
//            ),
//            id = 1,
//            coord = Coord(
//                lon = 31.2357,
//                lat = 30.0444
//            ),
//            dt = 1618317040L,
//            sys = Sys(
//                country = "EG",
//                sunrise = 1618282134L,
//                sunset = 1618333901L
//            ),
//            clouds = Clouds(
//                all = 0
//            )
//        )
//
//
//        // when
//        val result = repository.getWeatherByCityName(city, apiKey)
//
//        // then
//
//        assertThat(result, `is`(fakeWeatherResponse))
//    }
//
//}
//
