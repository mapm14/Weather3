package manuelperera.com.weather3.domain.service

import manuelperera.com.weather3.domain.model.ForecastModel
import manuelperera.com.weather3.domain.objects.City
import manuelperera.com.weather3.domain.objects.Coordinates
import manuelperera.com.weather3.domain.objects.ForecastByCity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ForecastServiceUnitTest {

    @Mock
    private
    lateinit var forecastModel: ForecastModel

    private val forecastByCityData: ForecastByCity = ForecastByCity(City("", "", Coordinates("", ""), ""), listOf())

    private lateinit var forecastService: ForecastService

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        forecastService = ForecastService(forecastModel)
    }

    @Test
    fun getForecasts() {
        forecastService.setLatAndLon("40.00", "-3.13").andThen {
            val testObserver = forecastService.getForecasts().test()

            testObserver.assertComplete()
            testObserver.assertValueCount(1)
            testObserver.assertValue { transaction ->
                transaction.data == forecastByCityData && transaction.isSuccess()
            }
        }
    }

}