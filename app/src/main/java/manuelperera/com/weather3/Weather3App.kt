package manuelperera.com.weather3

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import manuelperera.com.weather3.injection.AppComponent
import manuelperera.com.weather3.injection.DaggerAppComponent
import manuelperera.com.weather3.injection.module.AppModule

class Weather3App : Application() {

    companion object {
        lateinit var mDaggerAppComponent: AppComponent
        fun daggerAppComponent() = mDaggerAppComponent
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        mDaggerAppComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

}