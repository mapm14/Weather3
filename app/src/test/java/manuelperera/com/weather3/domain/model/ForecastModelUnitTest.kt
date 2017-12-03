package manuelperera.com.weather3.domain.model

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import manuelperera.com.base.client.transaction.Transaction
import manuelperera.com.base.client.transaction.TransactionStatus
import manuelperera.com.weather3.domain.objects.City
import manuelperera.com.weather3.domain.objects.Coordinates
import manuelperera.com.weather3.domain.objects.ForecastByCity
import manuelperera.com.weather3.domain.repository.api.ForecastApiRepository
import manuelperera.com.weather3.domain.repository.cache.ForecastCacheRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ForecastModelUnitTest {

    @Mock private
    lateinit var forecastApiRepository: ForecastApiRepository

    @Mock private
    lateinit var forecastCacheRepository: ForecastCacheRepository

    private lateinit var forecastModel: ForecastModel

    private val forecastByCityData: ForecastByCity = ForecastByCity(City("", "", Coordinates("", ""), ""), listOf())

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        forecastModel = ForecastModel(forecastApiRepository, forecastCacheRepository)
    }

    @Test
    fun getForecasts() {
        whenever(forecastApiRepository.getForecasts("40.00", "-3.13")).doReturn(Observable.create { observer ->
            observer.onNext(Transaction(forecastByCityData, TransactionStatus.SUCCESS))
            observer.onComplete()
        })

        whenever(forecastCacheRepository.getForecasts()).doReturn(Observable.create { observer ->
            observer.onNext(Transaction(forecastByCityData, TransactionStatus.SUCCESS))
            observer.onComplete()
        })

        val testObserver = forecastModel.getForecasts("40.00", "-3.13").test()

        testObserver.assertComplete()
        testObserver.assertValueCount(1)
        testObserver.assertValue { transaction ->
            transaction.data == forecastByCityData && transaction.isSuccess()
        }
    }
}