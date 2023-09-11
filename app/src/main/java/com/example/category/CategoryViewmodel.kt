package com.example.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.data.Product
import com.example.model.repository.ProductRepository
import kotlinx.coroutines.launch

class CategoryViewmodel(private val productRepository: ProductRepository,):ViewModel() {

    val dataproducts = mutableStateOf<List<Product>>(listOf())

            fun loaddatabycategory(category:String){

                viewModelScope.launch {
                   val datafromlocal =  productRepository.getallproductsbyCategory(category)
                    dataproducts.value = datafromlocal
                }
            }
}