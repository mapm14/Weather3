package manuelperera.com.base.screen.presenter.recyclerview

interface RecyclerViewAdapterItem {

    fun setType(type: Type)

    fun getType(): Type

    enum class Type {
        HEADER, ITEM, FULLSCREEN_LOADING, LOADING, FULLSCREEN_ERROR, ERROR, FOOTER, EMPTY
    }

}