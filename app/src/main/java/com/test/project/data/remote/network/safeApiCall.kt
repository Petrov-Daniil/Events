package com.test.project.data.remote.network

import com.squareup.moshi.*
import com.test.project.domain.RequestResult
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.*
import java.nio.channels.ClosedChannelException
import javax.net.ssl.SSLException


    inline fun <reified T> convert(jsonList: List<String>): MutableList<T> {
        val jsonAdapter = Moshi.Builder().build().adapter(T::class.java)
        val result: MutableList<T> = mutableListOf()
        for(json in jsonList){
            jsonAdapter.fromJson(json)?.let { result.add(it) }
        }
        return result
    }

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): RequestResult<T> {
        return try {
            RequestResult.Success(apiCall())
        } catch (throwable: Exception) {
            when (throwable) {
                is HttpException -> RequestResult.Error(
                    (mapError(
                        throwable
                    ))
                )
                is CancellationException -> RequestResult.Error(
                    mapError(
                        throwable
                    )
                )
                is JsonDataException, is JsonEncodingException -> RequestResult.Error(
                    mapError(
                        throwable
                    )
                )
                else -> {
                    RequestResult.Error(
                        if (hasConnectivityIssue(
                                throwable
                            )
                        ) mapError(throwable)
                        else error(throwable)

                    )
                }
            }
        }
    }


    private fun mapError(httpException: HttpException) = when (httpException.code()) {
        401 -> NetworkErrors.Http.Unauthorized(fromHttpException())
        400, 403, 405, 406 -> NetworkErrors.Http.BadRequest(fromHttpException())
        404 -> NetworkErrors.Http.NotFound(fromHttpException())
        408 -> NetworkErrors.Http.Timeout(fromHttpException())
        429 -> NetworkErrors.Http.LimitRateSuppressed(fromHttpException())
        422 -> NetworkErrors.Http.Unauthorized(fromHttpException())
        in 500..599 -> NetworkErrors.Http.InternalServerError(fromHttpException())
        else -> NetworkErrors.Http.Generic(fromHttpException())
    }

    private fun mapError(cancellationException: CancellationException) =
        NetworkErrors.Cancellation.Cancelled

    fun fromHttpException(httpException: HttpException? = null) =
        ApiErrorMessage(message = "Unable to parse API error")

    private fun hasConnectivityIssue(throwable: Throwable): Boolean = throwable.isNetworkException()

    private fun mapError(throwable: Throwable): NetworkErrors.Connectivity = when (throwable) {
        is SocketTimeoutException -> NetworkErrors.Connectivity.Timeout
        is BindException -> NetworkErrors.Connectivity.HostUnreachable
        is ClosedChannelException -> NetworkErrors.Connectivity.HostUnreachable
        is ConnectException -> NetworkErrors.Connectivity.HostUnreachable
        is NoRouteToHostException -> NetworkErrors.Connectivity.HostUnreachable
        is PortUnreachableException -> NetworkErrors.Connectivity.HostUnreachable
        is InterruptedIOException -> NetworkErrors.Connectivity.FailedConnection
        is UnknownServiceException -> NetworkErrors.Connectivity.FailedConnection
        is UnknownHostException -> NetworkErrors.Connectivity.FailedConnection
        is ProtocolException -> NetworkErrors.Connectivity.BadConnection
        is SocketException -> NetworkErrors.Connectivity.BadConnection
        is SSLException -> NetworkErrors.Connectivity.BadConnection
        else -> NetworkErrors.Connectivity.Generic
    }

    fun Throwable.isNetworkException(): Boolean =
        this is BindException ||
                this is ClosedChannelException ||
                this is ConnectException ||
                this is InterruptedIOException ||
                this is NoRouteToHostException ||
                this is PortUnreachableException ||
                this is ProtocolException ||
                this is SocketException ||
                this is SocketTimeoutException ||
                this is SSLException ||
                this is UnknownHostException ||
                this is UnknownServiceException