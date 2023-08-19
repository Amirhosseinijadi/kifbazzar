package com.example.signin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.repository.UserRepository
import kotlinx.coroutines.launch

class SignInviewmodel(private val userRepository: UserRepository):ViewModel() {

    val email = MutableLiveData("")
    val password = MutableLiveData("")



    fun signinuser(LogingEvent : (String)->Unit){

        viewModelScope.launch {
            val result = userRepository.signIn(email.value!!,password.value!!)
            LogingEvent(result)
        }





    }
}