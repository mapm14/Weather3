package manuelperera.com.weather3.injection

import android.content.Context
import dagger.Component
import manuelperera.com.weather3.injection.module.AppModule
import manuelperera.com.weather3.injection.module.client.ApiModule
import manuelperera.com.weather3.injection.module.client.TransactionModule
import manuelperera.com.weather3.injection.module.domain.model.ModelModule
import manuelperera.com.weather3.injection.module.domain.repository.ApiRepositoryModule
import manuelperera.com.weather3.injection.module.domain.repository.CacheRepositoryModule
import manuelperera.com.weather3.injection.module.domain.service.BusinessServiceModule
import manuelperera.com.weather3.injection.module.usecase.ForecastUseCaseModule
import manuelperera.com.weather3.usecase.forecast.ClearCacheForecastUseCase
import manuelperera.com.weather3.usecase.forecast.GetForecastsUseCase
import manuelperera.com.weather3.usecase.forecast.SetLatAndLonUseCase
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (AppModule::class),
    (ApiModule::class),
    (TransactionModule::class),
    (BusinessServiceModule::class),
    (ModelModule::class),
    (ApiRepositoryModule::class),
    (CacheRepositoryModule::class),
    (ForecastUseCaseModule::class)])
interface AppComponent {

    fun provideContext(): Context

    // Use Case

    fun provideGetForecastsUseCase(): GetForecastsUseCase

    fun provideSetLatAndLonUseCase(): SetLatAndLonUseCase

    fun provideClearCacheForecastUseCase(): ClearCacheForecastUseCase

}