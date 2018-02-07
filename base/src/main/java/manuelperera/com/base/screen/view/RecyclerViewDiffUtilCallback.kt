package manuelperera.com.base.screen.view

import android.support.v7.util.DiffUtil

class RecyclerViewDiffUtilCallback<T>(private var mOldList: MutableList<T>,
                                      private var mNewList: MutableList<T>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = mOldList.size

    override fun getNewListSize(): Int = mNewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            mNewList[newItemPosition] == mOldList[oldItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            mNewList[newItemPosition] == mOldList[oldItemPosition]

}