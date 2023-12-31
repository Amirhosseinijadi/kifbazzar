package com.example.model.repository

import com.example.model.data.Ads
import com.example.model.data.Product

interface ProductRepository {

    suspend fun getallproducts(isInternetconnected:Boolean):List<Product>

    suspend fun getallads(isInternetconnected:Boolean):List<Ads>

    suspend fun getallproductsbyCategory(category:String):List<Product>

    suspend fun getProductbyID(productId:String):Product
}