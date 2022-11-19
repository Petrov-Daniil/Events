package com.test.project.data.remote.network

sealed class Urls {

    class RELEASE: Urls() {
        val BASE_URL = "https://sibgutiapp.herokuapp.com/"
    }

}
