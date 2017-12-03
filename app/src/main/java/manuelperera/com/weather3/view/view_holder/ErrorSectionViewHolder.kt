package manuelperera.com.weather3.view.view_holder

import android.view.View

class ErrorSectionViewHolder<T>(val view: View, private val reloadAction: (View) -> Unit) : RecyclerViewViewHolder<T>(view) {

    override fun configure(item: T?) {
        reloadAction(view)
    }

}