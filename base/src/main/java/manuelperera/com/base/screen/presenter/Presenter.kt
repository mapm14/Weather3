package manuelperera.com.base.screen.presenter

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class Presenter<V : PresenterView> {
    private var mCompositeDisposable: CompositeDisposable? = null
    var view: V? = null

    fun init(presenterView: V) {
        view = presenterView
        mCompositeDisposable = CompositeDisposable()
        init()
    }

    fun clear() {
        view = null
        mCompositeDisposable?.dispose()
    }

    protected fun addSubscription(disposable: Disposable) {
        mCompositeDisposable?.add(disposable)
    }

    protected abstract fun init()
}