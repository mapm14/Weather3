package manuelperera.com.base.client.transaction

import manuelperera.com.base.domain.model.ErrorBody

class Transaction<T>(var data: T? = null,
                     var status: TransactionStatus? = null,
                     val errorBody: ErrorBody? = null) {

    fun isSuccess(): Boolean =
            status == TransactionStatus.SUCCESS

}