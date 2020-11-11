package com.library.base.net

import com.aleyn.mvvm.network.interceptor.LoggingInterceptor
import com.library.base.config.Constant
import com.library.base.net.inteceptor.AuthInterceptor
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class RetrofitClient {

    fun createRetrofit(){
        retrofit = Retrofit.Builder()
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.BASE_URL).build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(300L, TimeUnit.SECONDS)
                .addNetworkInterceptor(AuthInterceptor())
                .addNetworkInterceptor(LoggingInterceptor())
                .writeTimeout(300L, TimeUnit.SECONDS)
                .connectionPool(ConnectionPool(8, 15, TimeUnit.SECONDS))
                .build()
    }

    private object SingleRetorfit {
        val INSTANCE = RetrofitClient()
    }

    companion object {
        fun getInstance() = SingleRetorfit.INSTANCE
        private  var retrofit: Retrofit? =null
    }

    fun updateBaseUrl(ip: String) {
        Constant.setBaseUrl(ip)
        createRetrofit()
    }

    fun <T> create(service: Class<T>?): T {
        if(retrofit==null){
            synchronized(this){
                if(retrofit==null){
                    createRetrofit();
                }
            }
        }
       return  retrofit!!.create(service!!) ?: throw RuntimeException("api is null ")
    }
}