package manuelperera.com.weather3.client.transaction

import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.ObservableSource
import manuelperera.com.base.BuildConfig
import manuelperera.com.base.client.transaction.Transaction
import manuelperera.com.base.client.transaction.TransactionRequest
import manuelperera.com.base.client.transaction.TransactionStatus
import manuelperera.com.base.domain.model.ErrorBody
import manuelperera.com.weather3.R
import manuelperera.com.weather3.Weather3App
import okhttp3.ResponseBody
import retrofit2.adapter.rxjava2.Result
import java.util.concurrent.TimeUnit

class TransactionRequestImpl<T : Any> : TransactionRequest<T> {

    private val context = Weather3App.mDaggerAppComponent.provideContext()

    private val errorCode = -1
    private val networkErrorCode = 408
    private val errorMessage = context.getString(R.string.ups_error_message)
    private val networkErrorMessage = context.getString(R.string.network_error_message)
    private val unknownErrorMessage = context.getString(R.string.unknown_error_message)

    override fun modifyObservable(observable: Observable<Result<T>>): Observable<Transaction<T>> {
        return observable
                .flatMap { data ->
                    ObservableSource<Transaction<T>> { observer ->
                        if (data.response() != null) {
                            val response = data.response()!!

                            val body: T? = response.body()
                            val code = response.code()

                            if (code in 200..208 && body != null) {
                                observer.onNext(Transaction(body, TransactionStatus.SUCCESS))
                            } else if (code in 200..208 && body == null) {
                                observer.onNext(Transaction(status = TransactionStatus.SUCCESS))
                            } else if (code in 400..512) {
                                val errorBody = parseErrorBody(response.errorBody())
                                observer.onNext(Transaction(status = TransactionStatus.EXCEPTION, errorBody = errorBody))
                            } else {
                                val errorBody = ErrorBody(errorCode, errorMessage)
                                observer.onNext(Transaction(status = TransactionStatus.EXCEPTION, errorBody = errorBody))
                            }
                        } else {
                            val errorBody = ErrorBody(networkErrorCode, networkErrorMessage)
                            observer.onNext(Transaction(status = TransactionStatus.TIMEOUT, errorBody = errorBody))
                        }

                        observer.onComplete()
                    }
                }
                .timeout(BuildConfig.TIME_OUT, TimeUnit.SECONDS, Observable.create<Transaction<T>> { subscriber ->
                    val errorBody = ErrorBody(networkErrorCode, networkErrorMessage)
                    subscriber.onNext(Transaction(status = TransactionStatus.TIMEOUT, errorBody =  errorBody))
                    subscriber.onComplete()
                })
    }

    override fun modifyObservableList(observable: Observable<Result<List<T>>>): Observable<Transaction<List<T>>> {
        return observable
                .flatMap { data ->
                    ObservableSource<Transaction<List<T>>> { observer ->
                        if (data.response() != null) {
                            val response = data.response()!!

                            val body: List<T>? = response.body()
                            val code = response.code()

                            if (code in 200..208 && body != null) {
                                observer.onNext(Transaction(body, TransactionStatus.SUCCESS))
                            } else if (code in 200..208 && body == null) {
                                observer.onNext(Transaction(status = TransactionStatus.SUCCESS))
                            } else if (code in 400..512) {
                                val errorBody = parseErrorBody(response.errorBody())
                                observer.onNext(Transaction(status = TransactionStatus.EXCEPTION, errorBody = errorBody))
                            } else {
                                val errorBody = ErrorBody(errorCode, errorMessage)
                                observer.onNext(Transaction(status = TransactionStatus.EXCEPTION, errorBody = errorBody))
                            }
                        } else {
                            val errorBody = ErrorBody(networkErrorCode, networkErrorMessage)
                            observer.onNext(Transaction(status = TransactionStatus.TIMEOUT, errorBody =  errorBody))
                        }

                        observer.onComplete()
                    }
                }
                .timeout(BuildConfig.TIME_OUT, TimeUnit.SECONDS, Observable.create<Transaction<List<T>>> { subscriber ->
                    val errorBody = ErrorBody(networkErrorCode, networkErrorMessage)
                    subscriber.onNext(Transaction(status = TransactionStatus.TIMEOUT, errorBody = errorBody))
                    subscriber.onComplete()
                })

    }

    private fun parseErrorBody(responseBody: ResponseBody?): ErrorBody? {
        responseBody?.let { responseErrorBody ->
            return Gson().fromJson(responseErrorBody.string(), ErrorBody::class.java)
        }

        return null
    }
}