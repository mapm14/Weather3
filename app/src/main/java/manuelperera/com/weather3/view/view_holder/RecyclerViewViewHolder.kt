package manuelperera.com.weather3.view.view_holder

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class RecyclerViewViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun configure(item: T? = null)

}