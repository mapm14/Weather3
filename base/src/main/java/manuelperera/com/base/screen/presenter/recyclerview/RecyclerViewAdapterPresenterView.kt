package manuelperera.com.base.screen.presenter.recyclerview

import android.support.v7.util.DiffUtil
import manuelperera.com.base.screen.presenter.PresenterView

interface RecyclerViewAdapterPresenterView: PresenterView {

    fun getDiffResultBinder(diffResult: DiffUtil.DiffResult): DiffUtil.DiffResult

}