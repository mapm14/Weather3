package manuelperera.com.weather3.screen.main.section

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import io.reactivex.Observable
import kotlinx.android.synthetic.main.item_error.view.*
import manuelperera.com.base.screen.presenter.recyclerview.RecyclerViewAdapterItem
import manuelperera.com.weather3.R
import manuelperera.com.weather3.Weather3App
import manuelperera.com.weather3.domain.objects.Forecast
import manuelperera.com.weather3.extensions.convertDpToPixel
import manuelperera.com.weather3.extensions.inflate
import manuelperera.com.weather3.screen.main.ForecastActivityPresenterView
import manuelperera.com.weather3.screen.main.section.injection.DaggerForecastActivityRecyclerAdapterComponent
import manuelperera.com.weather3.view.view_holder.ErrorSectionViewHolder
import manuelperera.com.weather3.view.view_holder.LoadingSectionViewHolder
import manuelperera.com.weather3.view.view_holder.RecyclerViewViewHolder
import manuelperera.com.weather3.view.widget.ForecastChromeView
import javax.inject.Inject

class ForecastActivityRecyclerAdapter(private val forecastActivityPresenterView: ForecastActivityPresenterView) : RecyclerView.Adapter<RecyclerViewViewHolder<Forecast>>(), ForecastActivityRecyclerAdapterView {

    @Inject
    lateinit var forecastActivityRecyclerAdapterPresenter: ForecastActivityRecyclerAdapterPresenter

    init {
        DaggerForecastActivityRecyclerAdapterComponent.builder().appComponent(Weather3App.Companion.daggerAppComponent()).build().inject(this)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        forecastActivityRecyclerAdapterPresenter.init(this)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        forecastActivityRecyclerAdapterPresenter.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder<Forecast> =
            when (viewType) {
                RecyclerViewAdapterItem.Type.ITEM.ordinal -> ForecastViewHolder(ForecastChromeView(parent.context), forecastActivityRecyclerAdapterPresenter)
                RecyclerViewAdapterItem.Type.LOADING.ordinal -> LoadingSectionViewHolder((parent.inflate(R.layout.item_loading)))
                else -> ErrorSectionViewHolder((parent.inflate(R.layout.item_error)), { view ->
                    forecastActivityRecyclerAdapterPresenter.bindReloadDataObservable(view.networkErrorRetryButton.asObservable())
                })
            }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder<Forecast>, position: Int) {
        holder.configure(forecastActivityRecyclerAdapterPresenter.listData[position])
    }

    override fun getItemCount(): Int = forecastActivityRecyclerAdapterPresenter.listData.size

    override fun getItemViewType(position: Int): Int = forecastActivityRecyclerAdapterPresenter.listData[position].rType.ordinal

    override fun getDiffResultBinder(diffResult: DiffUtil.DiffResult): DiffUtil.DiffResult {
        diffResult.dispatchUpdatesTo(this@ForecastActivityRecyclerAdapter)
        return diffResult
    }

    override fun finishLoad(isSuccess: Boolean) {
        if (isSuccess)
            forecastActivityPresenterView.getCurrentForecast()
        forecastActivityPresenterView.getSwipeRefreshLayout().isRefreshing = false
    }

    private class ForecastViewHolder(view: View, val presenter: ForecastActivityRecyclerAdapterPresenter) : RecyclerViewViewHolder<Forecast>(view) {
        override fun configure(item: Forecast?) {
            val forecastChromeView: ForecastChromeView = (itemView as ForecastChromeView)
            forecastChromeView.setForecastChrome(item!!)
            presenter.bindItemClick(itemView, presenter.listData[adapterPosition])

            val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.topMargin = convertDpToPixel(16f).toInt()
            layoutParams.marginStart = convertDpToPixel(4f).toInt()
            layoutParams.marginEnd = convertDpToPixel(4f).toInt()
            forecastChromeView.layoutParams = layoutParams
        }
    }

    fun reloadAdapter() {
        forecastActivityRecyclerAdapterPresenter.listData.clear()
        forecastActivityRecyclerAdapterPresenter.bindReloadDataObservable(Observable.create { observer ->
            observer.onNext(true)
            observer.onComplete()
        })
    }

}