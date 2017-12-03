package manuelperera.com.weather3.injection.module.domain.repository

import dagger.Module
import dagger.Provides
import manuelperera.com.base.client.transaction.TransactionRequestFactory
import manuelperera.com.weather3.client.apiclient.ForecastApiClient
import manuelperera.com.weather3.domain.objects.ForecastByCity
import manuelperera.com.weather3.domain.repository.api.ForecastApiRepository

@Module
class ApiRepositoryModule {

    @Provides
    fun forecastApiRepository(forecastApiClient: ForecastApiClient,
                              forecastByCityTransactionRequestFactory: TransactionRequestFactory<ForecastByCity>): ForecastApiRepository =
            ForecastApiRepository(forecastApiClient, forecastByCityTransactionRequestFactory)

}