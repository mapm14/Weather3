package manuelperera.com.weather3.screen.main.section

import android.view.View
import io.reactivex.Completable
import io.reactivex.Observable
import manuelperera.com.base.client.transaction.Transaction
import manuelperera.com.base.screen.presenter.recyclerview.RecyclerViewAdapterItem
import manuelperera.com.base.screen.presenter.recyclerview.SimpleRecyclerViewAdapterPresenter
import manuelperera.com.weather3.domain.objects.Forecast
import manuelperera.com.weather3.usecase.forecast.GetForecastsUseCase

class ForecastActivityRecyclerAdapterPresenter(private val getForecastsUseCase: GetForecastsUseCase) : SimpleRecyclerViewAdapterPresenter<ForecastActivityRecyclerAdapterView, Forecast>() {

    override fun getLoadObservable(): Observable<Transaction<List<Forecast>>> = getForecastsUseCase.bind().map { transaction ->
        val mutableForecasts = transaction.data?.toMutableList()
        mutableForecasts?.removeAt(0)
        transaction.data = mutableForecasts
        transaction
    }.doOnNext { transaction ->
        view?.finishLoad(transaction.isSuccess())
    }

    override fun getItemClickCompletable(viewClicked: View, data: Forecast): Completable =
            Completable.create {
                it.onComplete()
            }

    override fun getLoadingList(): MutableList<Forecast> = mutableListOf(Forecast(RecyclerViewAdapterItem.Type.LOADING))

    override fun getNetworkErrorList(): MutableList<Forecast> = mutableListOf(Forecast(RecyclerViewAdapterItem.Type.ERROR))

}