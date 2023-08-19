package com.example.model.repository

import android.content.SharedPreferences
import com.example.model.net.ApiService
import com.example.utill.VALUE_SUCCESS
import com.google.gson.JsonObject

class UserRepositoryIMPL(
    private val apiservice: ApiService, private val sharedpref: SharedPreferences
) : UserRepository {
    override suspend fun signUp(name: String, username: String, password: String):String {

        val jsonobject = JsonObject().apply {
            addProperty("name",name)
            addProperty("email",username)
            addProperty("password",password)
        }

       val result =  apiservice.signUp(jsonobject)

        if(result.success){

            TokenInMemory.refreshToken(username,result.token)
            saveUsername(username)
            saveToken(result.token)

         return VALUE_SUCCESS
        }else{
            return result.message
        }


    }

    override suspend fun signIn(username: String, password: String):String {

        val jsonobject = JsonObject().apply {
            addProperty("email",username)
            addProperty("password",password)
        }
        val result = apiservice.signIn(jsonobject)
        if (result.success){
            TokenInMemory.refreshToken(username,result.token)
            saveUsername(username)
            saveToken(result.token)
            return VALUE_SUCCESS
        }else{
            return result.message
        }


    }

    override fun signOut() {
        TokenInMemory.refreshToken(null,null)
        sharedpref.edit().clear().apply()

    }

    override fun loadToken() {
       TokenInMemory.refreshToken(getUsername(),getToken())
    }

    override fun saveToken(newToken: String) {

        sharedpref.edit().putString("token",newToken).apply()

    }

    override fun getToken(): String? {

        return sharedpref.getString("token",null)!!
    }

    override fun saveUsername(username: String) {

        sharedpref.edit().putString("username",username).apply()
    }

    override fun getUsername(): String? {
        return sharedpref.getString("username",null)!!
    }

}