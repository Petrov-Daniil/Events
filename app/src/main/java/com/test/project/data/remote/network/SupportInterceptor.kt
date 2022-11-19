package com.test.project.data.remote.network

import okhttp3.Interceptor
import okhttp3.Response

class SupportInterceptor(
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {


        var request = chain.request()
        request = request.newBuilder().apply {
        }.build()
        return chain.proceed(request)
    }
}