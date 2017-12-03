package manuelperera.com.weather3.injection.module.client

import dagger.Module
import dagger.Provides
import manuelperera.com.base.client.transaction.TransactionRequestFactory
import manuelperera.com.weather3.client.transaction.TransactionRequestFactoryImpl
import manuelperera.com.weather3.domain.objects.ForecastByCity
import javax.inject.Singleton

@Module
class TransactionModule {

    @Provides
    fun forecastByCityTransactionRequestFactory(): TransactionRequestFactory<ForecastByCity> =
            TransactionRequestFactoryImpl()

}