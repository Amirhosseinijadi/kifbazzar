package com.example.model.net

import android.annotation.SuppressLint
import com.example.model.data.LoginResponse
import com.example.model.repository.TokenInMemory
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Retrofit

class Autchecker:Authenticator,KoinComponent {
    private val apiservice:ApiService by inject()
    override fun authenticate(route: Route?, response: Response): Request? {

        if(TokenInMemory.token != null && response.request.url.pathSegments.last().equals("refreshtoken",false)){

            val result = refreshToken()
            if (result){

                return response.request

            }




        }

        return null



    }

    @SuppressLint("SuspiciousIndentation")
    fun refreshToken():Boolean{
      val request:retrofit2.Response<LoginResponse> = apiservice.refreshtoken().execute()
        if (request.body() != null){

            if(request.body()!!.success){

                return true

            }

        }
        return false
    }
}