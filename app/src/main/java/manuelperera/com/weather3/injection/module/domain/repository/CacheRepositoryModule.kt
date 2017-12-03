package manuelperera.com.weather3.injection.module.domain.repository

import dagger.Module
import dagger.Provides
import manuelperera.com.weather3.domain.repository.cache.ForecastCacheRepository
import javax.inject.Singleton

@Module
class CacheRepositoryModule {

    @Provides
    @Singleton
    fun forecastCacheRepository(): ForecastCacheRepository =
            ForecastCacheRepository()

}