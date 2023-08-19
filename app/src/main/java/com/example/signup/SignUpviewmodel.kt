package com.example.signup

import android.app.usage.UsageEvents
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kifbazar.R
import com.example.model.repository.UserRepository
import kotlinx.coroutines.launch

class SignUpviewmodel(private val userRepository: UserRepository):ViewModel() {
    val name = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val confirmpassword = MutableLiveData("")


    fun signUpuser(LoginEvent:(String) -> Unit ){
      viewModelScope.launch {

          val result =  userRepository.signUp(name.value!!,email.value!!, password.value!!)

          LoginEvent(result)
      }







    }
}