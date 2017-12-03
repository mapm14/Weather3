package manuelperera.com.base.client.transaction

interface TransactionRequestFactory<T : Any> {

    fun createTransactionRequest(): TransactionRequest<T>

}