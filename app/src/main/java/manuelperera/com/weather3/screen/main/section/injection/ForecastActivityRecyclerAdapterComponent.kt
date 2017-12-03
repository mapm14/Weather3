package manuelperera.com.weather3.screen.main.section.injection

import dagger.Component
import manuelperera.com.weather3.injection.AppComponent
import manuelperera.com.weather3.screen.main.section.ForecastActivityRecyclerAdapter

@ForecastActivityRecyclerAdapterScope
@Component(dependencies = arrayOf(AppComponent::class), modules = [(ForecastActivityRecyclerAdapterModule::class)])
interface ForecastActivityRecyclerAdapterComponent {

    fun inject(forecastActivityRecyclerAdapter: ForecastActivityRecyclerAdapter)

}