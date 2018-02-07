package manuelperera.com.base.usecase

interface UseCase<out T> {

    fun bind(): T

}