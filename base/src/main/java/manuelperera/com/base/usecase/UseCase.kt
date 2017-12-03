package manuelperera.com.base.usecase

interface UseCase<T> {

    abstract fun bind(): T

}