package manuelperera.com.base.screen.presenter.recyclerview

import android.support.v7.util.DiffUtil
import android.view.View
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import manuelperera.com.base.client.transaction.Transaction
import manuelperera.com.base.client.transaction.TransactionStatus
import manuelperera.com.base.screen.presenter.AdapterPresenter
import java.util.*

abstract class RecyclerViewAdapterPresenter<V : RecyclerViewAdapterPresenterView, T : RecyclerViewAdapterItem> : AdapterPresenter<V>() {

    var listData: MutableList<T> = mutableListOf()
    private var mLoadingList: List<T>? = null
    private var mNetworkErrorList: List<T>? = null

    override fun init() {
        initLists()
        initialize()
        loadData()
    }

    override fun initWithoutLoad() {
        initLists()
        initialize()
    }

    fun bindItemClick(view: View, data: T) {
        addSubscription(RxView.clicks(view)
                .flatMapCompletable({ getItemClickCompletable(view, data) })
                .subscribe())
    }

    fun bindReloadDataObservable(reloadObservable: Observable<Any>) {
        addSubscription(reloadObservable
                .flatMap<Transaction<List<T>>> { loadLoadingData() }
                .flatMap<DiffUtil.DiffResult>(calculateRecyclerViewDiffs())
                .doOnNext(showResults())
                .flatMap<DiffUtil.DiffResult> { load() }
                .subscribe(showResults()))
    }

    fun bindClearDataObservable(clearObservable: Observable<Any>) {
        addSubscription(clearObservable
                .flatMap<Transaction<List<T>>> { loadEmptyData() }
                .flatMap<DiffUtil.DiffResult>(calculateRecyclerViewDiffs())
                .subscribe(showResults()))
    }

    private fun load(): Observable<DiffUtil.DiffResult> {
        return loadObservableData()
                .flatMap<DiffUtil.DiffResult>(calculateRecyclerViewDiffs())
    }

    private fun showResults(): Consumer<DiffUtil.DiffResult> {
        return Consumer { diffResult -> getDiffResultBinder(diffResult) }
    }

    fun hasLoadingView(): Boolean? {
        return mLoadingList != null && !mLoadingList!!.isEmpty()
    }

    private fun loadLoadingData(): Observable<Transaction<List<T>>> {
        return Observable.create<Transaction<List<T>>> { observer ->
            observer.onNext(Transaction(mLoadingList, TransactionStatus.SUCCESS))
            observer.onComplete()
        }
    }

    private fun calculateRecyclerViewDiffs(): Function<Transaction<List<T>>, Observable<DiffUtil.DiffResult>> {
        return Function { transaction ->
            Observable.create<DiffUtil.DiffResult> { observer ->
                observer.onNext(calculateRecyclerViewDiffResult(if (transaction.isSuccess()) transaction.data else mNetworkErrorList))
                observer.onComplete()
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun loadData() {
        addSubscription(load().subscribe(showResults()))
    }

    private fun loadObservableData(): Observable<Transaction<List<T>>> {
        return getLoadObservable().map<Transaction<List<T>>>(addAdapterItemTypeToElements())
    }

    private fun addAdapterItemTypeToElements(): Function<Transaction<List<T>>, Transaction<List<T>>> {
        return Function { transaction ->
            val newTransaction: Transaction<List<T>>

            if (transaction.isSuccess() && transaction.data != null) {
                val list = ArrayList<T>()

                transaction.data?.let { data ->
                    for (element in data) {
                        if (element.getType() !== RecyclerViewAdapterItem.Type.HEADER && element.getType() !== RecyclerViewAdapterItem.Type.ITEM) {
                            element.setType(RecyclerViewAdapterItem.Type.ITEM)
                        }
                        list.add(element)
                    }
                }

                transaction.data = list
                newTransaction = transaction
            } else
                newTransaction = Transaction(status = TransactionStatus.ERROR)

            newTransaction
        }
    }

    private fun getDiffResultBinder(diffResult: DiffUtil.DiffResult) {
        view?.getDiffResultBinder(diffResult)
    }

    private fun initLists() {
        mLoadingList = getLoadingList()
        if (mLoadingList == null)
            mLoadingList = ArrayList()
        listData.addAll(mLoadingList!!)

        mNetworkErrorList = getNetworkErrorList()
        if (mNetworkErrorList == null)
            mNetworkErrorList = ArrayList()
    }

    private fun loadEmptyData(): Observable<Transaction<List<T>>> {
        return Observable.create<Transaction<List<T>>> { observer ->
            val emptyList = ArrayList<T>()
            observer.onNext(Transaction(emptyList, TransactionStatus.SUCCESS))
            observer.onComplete()
        }
    }

    abstract fun getLoadingList(): List<T>

    abstract fun getNetworkErrorList(): List<T>

    abstract fun getLoadObservable(): Observable<Transaction<List<T>>>

    abstract fun getItemClickCompletable(viewClicked: View, data: T): Completable

    abstract fun initialize()

    abstract fun calculateRecyclerViewDiffResult(newData: List<T>?): DiffUtil.DiffResult

}