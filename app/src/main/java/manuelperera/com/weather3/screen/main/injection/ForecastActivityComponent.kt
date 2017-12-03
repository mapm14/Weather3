package manuelperera.com.weather3.screen.main.injection

import dagger.Component
import manuelperera.com.weather3.injection.AppComponent
import manuelperera.com.weather3.screen.main.ForecastActivity

@ForecastActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = [(ForecastActivityModule::class)])
interface ForecastActivityComponent {

    fun inject(forecastActivity: ForecastActivity)

}