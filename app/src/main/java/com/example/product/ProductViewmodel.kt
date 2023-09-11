package com.example.product

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.data.Comment
import com.example.model.repository.CommentRepository
import com.example.model.repository.ProductRepository
import com.example.utill.EMPTY_PRODUCT
import com.example.utill.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductViewmodel(
    val productRepository:ProductRepository,
    val commentRepository: CommentRepository
):ViewModel() {
    val thisProduct = mutableStateOf(EMPTY_PRODUCT)
    val comments = mutableStateOf(listOf<Comment>())

    fun loaddata(productId:String,isInternetconnected:Boolean){
        loadProductFromcache(productId)
        if(isInternetconnected){
            getallComments(productId)
        }
    }


    fun loadProductFromcache(productId:String){
        viewModelScope.launch(coroutineExceptionHandler) {
            thisProduct.value =  productRepository.getProductbyID(productId)
        }


    }

    fun getallComments(productId: String){
        viewModelScope.launch(coroutineExceptionHandler) {
            comments.value = commentRepository.getAllComments(productId)
        }
    }
    fun addNewComment(productId: String, text: String, IsSuccess: (String) -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            commentRepository.addNewComment(productId, text, IsSuccess)
            delay(100)
            comments.value = commentRepository.getAllComments(productId)
        }
    }

}