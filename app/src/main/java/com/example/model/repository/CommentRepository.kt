package com.example.model.repository

import com.example.model.data.Comment

interface CommentRepository {

    suspend fun getAllComments(productId :String) :List<Comment>

    suspend fun addNewComment(productId: String, text: String, IsSuccess: (String) -> Unit)
}