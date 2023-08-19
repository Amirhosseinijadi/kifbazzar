package com.example.main


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.data.Ads
import com.example.model.data.Product
import com.example.model.repository.ProductRepository
import com.example.utill.CATEGORY
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val productRepository: ProductRepository
    ,isinternetconnected:Boolean
):ViewModel() {

    val dataproducts = mutableStateOf<List<Product>>(listOf())
    val dataads = mutableStateOf<List<Ads>>(listOf())
    val showprogressbar = mutableStateOf(false)

    init {
        refreshalldatafromnet(isinternetconnected)
    }

    fun refreshalldatafromnet(isinternetconnected: Boolean){

        viewModelScope.launch {
            if (isinternetconnected)
                showprogressbar.value = true
            delay(1200)
            val newdataproduct = async { productRepository.getallproducts(isinternetconnected) }
            val newdataads = async { productRepository.getallads(isinternetconnected) }
            updatedata(newdataproduct.await(),newdataads.await())



            showprogressbar.value = false


        }

    }

    private fun updatedata(product:List<Product>,ads:List<Ads>){

        dataproducts.value = product
        dataads.value = ads

    }


}