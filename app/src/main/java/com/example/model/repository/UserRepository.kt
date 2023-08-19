package com.example.model.repository

interface UserRepository {

    //online
    suspend fun signUp(name:String,username:String,password:String):String
    suspend fun signIn(username: String,password: String):String

    //offline
    fun signOut()

    fun loadToken()

    fun saveToken(newToken:String)

    fun getToken():String?

    fun saveUsername(username: String)

    fun getUsername():String?

}