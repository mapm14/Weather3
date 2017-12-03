package manuelperera.com.weather3.injection.module.domain.service

import dagger.Module
import dagger.Provides
import manuelperera.com.weather3.domain.model.ForecastModel
import manuelperera.com.weather3.domain.service.ForecastService
import javax.inject.Singleton

@Module
class BusinessServiceModule {

    @Provides
    @Singleton
    fun forecastService(forecastModel: ForecastModel): ForecastService =
            ForecastService(forecastModel)

}