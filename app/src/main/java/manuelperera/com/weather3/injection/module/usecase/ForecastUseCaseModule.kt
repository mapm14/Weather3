package manuelperera.com.weather3.injection.module.usecase

import dagger.Module
import dagger.Provides
import manuelperera.com.weather3.domain.service.ForecastService
import manuelperera.com.weather3.usecase.forecast.ClearCacheForecastUseCase
import manuelperera.com.weather3.usecase.forecast.GetForecastsUseCase
import manuelperera.com.weather3.usecase.forecast.SetLatAndLonUseCase

@Module
class ForecastUseCaseModule {

    @Provides
    fun getForecastsUseCase(forecastService: ForecastService): GetForecastsUseCase =
            GetForecastsUseCase(forecastService)

    @Provides
    fun setLatAndLonUseCase(forecastService: ForecastService): SetLatAndLonUseCase =
            SetLatAndLonUseCase(forecastService)

    @Provides
    fun clearCacheForecastUseCase(forecastService: ForecastService): ClearCacheForecastUseCase =
            ClearCacheForecastUseCase(forecastService)

}