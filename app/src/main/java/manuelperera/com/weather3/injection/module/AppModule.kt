package manuelperera.com.weather3.injection.module

import android.content.Context
import dagger.Module
import dagger.Provides
import manuelperera.com.weather3.Weather3App
import javax.inject.Singleton

@Module
class AppModule(private val app: Weather3App) {

    @Provides
    @Singleton
    fun context(): Context = app.applicationContext

}