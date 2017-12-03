package manuelperera.com.base.client.transaction

import io.reactivex.Observable
import retrofit2.adapter.rxjava2.Result

interface TransactionRequest<T> {

    fun modifyObservable(observable: Observable<Result<T>>): Observable<Transaction<T>>

    fun modifyObservableList(observable: Observable<Result<List<T>>>): Observable<Transaction<List<T>>>

}