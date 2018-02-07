package manuelperera.com.weather3.domain.repository

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import manuelperera.com.base.client.transaction.Transaction
import manuelperera.com.base.client.transaction.TransactionRequest
import manuelperera.com.base.client.transaction.TransactionRequestFactory
import manuelperera.com.base.client.transaction.TransactionStatus
import manuelperera.com.weather3.client.apiclient.ForecastApiClient
import manuelperera.com.weather3.domain.objects.City
import manuelperera.com.weather3.domain.objects.Coordinates
import manuelperera.com.weather3.domain.objects.ForecastByCity
import manuelperera.com.weather3.domain.repository.api.ForecastApiRepository
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import retrofit2.adapter.rxjava2.Result

@RunWith(MockitoJUnitRunner::class)
class ForecastApiRepositoryUnitTest {

    @Mock
    private lateinit var transactionRequest: TransactionRequest<ForecastByCity>

    @Mock
    private lateinit var transactionRequestFactory: TransactionRequestFactory<ForecastByCity>

    @Mock
    private lateinit var forecastApiClient: ForecastApiClient

    private lateinit var forecastApiRepository: ForecastApiRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        whenever(transactionRequestFactory.createTransactionRequest()).doReturn(transactionRequest)

        forecastApiRepository = ForecastApiRepository(forecastApiClient, transactionRequestFactory)
    }

    @Test
    fun getForecastsFromApiWithSuccess() {
        val forecastByCityData = ForecastByCity(City("", "", Coordinates("", ""), ""), listOf())

        whenever(forecastApiClient.getForecasts("40.00", "-3.13")).doReturn(Observable.create { observer ->
            observer.onNext(Result.response(Response.success(forecastByCityData)))
            observer.onComplete()
        })

        whenever(transactionRequest.modifyObservable(any())).doReturn(Observable.create { observer ->
            observer.onNext(Transaction(forecastByCityData, TransactionStatus.SUCCESS))
            observer.onComplete()
        })

        val testObserver = forecastApiRepository.getForecasts("40.00", "-3.13").test()

        testObserver.assertComplete()
        testObserver.assertValueCount(1)
        testObserver.assertValue { transaction ->
            transaction.data == forecastByCityData && transaction.isSuccess()
        }
    }

    @Test
    fun gesForecastsFromApiWithError() {
        whenever(forecastApiClient.getForecasts("40.00", "-3.13")).doReturn(Observable.create { observer ->
            observer.onComplete()
        })

        whenever(transactionRequest.modifyObservable(any())).doReturn(Observable.create { observer ->
            observer.onNext(Transaction(status = TransactionStatus.ERROR))
            observer.onComplete()
        })

        val testObserver = forecastApiRepository.getForecasts("40.00", "-3.13").test()

        testObserver.assertComplete()
        testObserver.assertValueCount(1)
        testObserver.assertValue { transaction ->
            transaction.data == null && transaction.status == TransactionStatus.ERROR
        }
    }

    @Test
    fun getForecastsFromApiWithTimeout() {
        whenever(forecastApiClient.getForecasts("40.00", "-3.13")).doReturn(Observable.create { observer ->
            observer.onNext(Result.response(Response.error(404, ResponseBody.create(MediaType.parse(""), ""))))
            observer.onComplete()
        })

        whenever(transactionRequest.modifyObservable(any())).doReturn(Observable.create { observer ->
            observer.onNext(Transaction(status = TransactionStatus.TIMEOUT))
            observer.onComplete()
        })

        val testObserver = forecastApiRepository.getForecasts("40.00", "-3.13").test()

        testObserver.assertComplete()
        testObserver.assertValueCount(1)
        testObserver.assertValue { transaction ->
            transaction.data == null && transaction.status == TransactionStatus.TIMEOUT
        }
    }
}