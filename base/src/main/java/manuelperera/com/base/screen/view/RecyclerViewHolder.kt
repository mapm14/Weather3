package manuelperera.com.base.screen.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

abstract class RecyclerViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun configure(context: Context, item: T)

}