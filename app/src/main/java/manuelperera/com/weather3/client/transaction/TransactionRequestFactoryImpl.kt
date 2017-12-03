package manuelperera.com.weather3.client.transaction

import manuelperera.com.base.client.transaction.TransactionRequest
import manuelperera.com.base.client.transaction.TransactionRequestFactory

class TransactionRequestFactoryImpl<T : Any> : TransactionRequestFactory<T> {

    override fun createTransactionRequest(): TransactionRequest<T> = TransactionRequestImpl()

}