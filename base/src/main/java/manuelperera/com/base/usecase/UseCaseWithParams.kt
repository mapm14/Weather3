package manuelperera.com.base.usecase

interface UseCaseWithParams<out T, in P : UseCaseParams> {

    fun bind(params: P): T

}