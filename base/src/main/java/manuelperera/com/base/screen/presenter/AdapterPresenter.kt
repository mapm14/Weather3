package manuelperera.com.base.screen.presenter

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class AdapterPresenter<V : PresenterView> {
    private var mCompositeDisposable: CompositeDisposable? = null
    var view: V? = null

    fun init(presenterView: V) {
        initPresenter(presenterView)
        init()
    }

    fun initWithoutLoad(presenterView: V) {
        initPresenter(presenterView)
        initWithoutLoad()
    }

    fun clear() {
        view = null
        mCompositeDisposable?.dispose()
    }

    protected fun addSubscription(disposable: Disposable) {
        mCompositeDisposable?.add(disposable)
    }

    protected abstract fun init()

    protected abstract fun initWithoutLoad()

    private fun initPresenter(presenterView: V) {
        view = presenterView
        mCompositeDisposable = CompositeDisposable()
    }
}