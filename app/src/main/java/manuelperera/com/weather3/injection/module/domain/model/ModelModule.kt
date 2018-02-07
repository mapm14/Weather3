package manuelperera.com.weather3.injection.module.domain.model

import dagger.Module
import dagger.Provides
import manuelperera.com.weather3.domain.model.ForecastModel
import manuelperera.com.weather3.domain.repository.api.ForecastApiRepository
import manuelperera.com.weather3.domain.repository.cache.ForecastCacheRepository

@Module
class ModelModule {

    @Provides
    fun forecastModel(forecastApiRepository: ForecastApiRepository, forecastCacheRepository: ForecastCacheRepository): ForecastModel =
            ForecastModel(forecastApiRepository, forecastCacheRepository)

}