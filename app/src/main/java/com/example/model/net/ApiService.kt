package com.example.model.net

import com.example.model.data.AdsResponse
import com.example.model.data.LoginResponse
import com.example.model.data.ProductResponse
import com.example.model.repository.TokenInMemory
import com.example.utill.BASE_URL
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("signUp")
    suspend fun signUp(@Body jsonObject: JsonObject):LoginResponse

    @POST("signIn")
   suspend fun signIn(@Body jsonObject: JsonObject):LoginResponse

   @GET
   fun refreshtoken():Call<LoginResponse>

   @GET("getProducts")
   suspend fun getallproducts():ProductResponse

   @GET("getSliderPics")
   suspend fun getallads():AdsResponse



}

fun createApiService():ApiService{

    val okhttpclient = OkHttpClient
        .Builder()
        .addInterceptor {
            val oldrequest = it.request()
            val newrequest = oldrequest.newBuilder()
            if(TokenInMemory.token != null)
                newrequest.addHeader("Authorization",TokenInMemory.token!!)
            newrequest.addHeader("Accept","application/json")
            newrequest.method(oldrequest.method,oldrequest.body)

            return@addInterceptor it.proceed(newrequest.build())

        }.build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okhttpclient)
        .build()

    return retrofit.create(ApiService::class.java)

}