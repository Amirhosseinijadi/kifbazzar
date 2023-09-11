package com.example.model.repository

import com.example.model.data.Comment
import com.example.model.net.ApiService
import com.google.gson.JsonObject

class CommentRepositoryIMPL(private val apiService: ApiService):CommentRepository {
    override suspend fun getAllComments(productId: String): List<Comment> {
       val jsonobject = JsonObject().apply {
           addProperty("productId",productId)
       }
       val data =  apiService.getAllComments(jsonobject)
        if(data.success){
            return data.comments
        }
        return listOf()
    }

    override suspend fun addNewComment(productId: String, text: String, IsSuccess: (String) -> Unit) {

        val jsonObject = JsonObject().apply {
            addProperty("productId" , productId)
            addProperty("text" , text)
        }
        val result = apiService.addNewComment(jsonObject)

        if(result.success) {
            IsSuccess.invoke(result.message)
        } else {
            IsSuccess.invoke("Comment not added")
        }

    }
}