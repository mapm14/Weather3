package manuelperera.com.base.screen.presenter.recyclerview

import android.support.v7.util.DiffUtil
import manuelperera.com.base.screen.view.RecyclerViewDiffUtilCallback

abstract class SimpleRecyclerViewAdapterPresenter<V : RecyclerViewAdapterPresenterView, T : RecyclerViewAdapterItem> : RecyclerViewAdapterPresenter<V, T>() {

    override fun initialize() {}

    override fun calculateRecyclerViewDiffResult(newData: List<T>?): DiffUtil.DiffResult {
        val oldList = ArrayList(listData)
        listData = ArrayList(newData)

        return DiffUtil.calculateDiff(RecyclerViewDiffUtilCallback(oldList, listData as ArrayList<T>))
    }

}