package manuelperera.com.weather3.screen.main.section

import manuelperera.com.base.screen.presenter.recyclerview.RecyclerViewAdapterPresenterView

interface ForecastActivityRecyclerAdapterView : RecyclerViewAdapterPresenterView {

    fun finishLoad(isSuccess: Boolean)

}