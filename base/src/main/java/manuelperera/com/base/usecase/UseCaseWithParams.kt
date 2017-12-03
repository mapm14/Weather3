package manuelperera.com.base.usecase

interface UseCaseWithParams<T, P : UseCaseParams> {

    abstract fun bind(params: P): T

}