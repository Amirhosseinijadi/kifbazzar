package com.example.model.repository

import com.example.model.data.Ads
import com.example.model.data.Product
import com.example.model.db.ProductDao
import com.example.model.net.ApiService

class ProductRepositoryIMPL(private val apiService: ApiService,private val productDao: ProductDao):ProductRepository {
    override suspend fun getallproducts(isInternetconnected:Boolean): List<Product> {

        if(isInternetconnected){
            //get from server
            val datafromapi = apiService.getallproducts()
           if (datafromapi.success){
               productDao.insertorupdate(datafromapi.products)
               return datafromapi.products
           }

        }else{
            //get from local
           return productDao.getall()
        }

        return listOf()

    }

    override suspend fun getallads(isInternetconnected:Boolean): List<Ads> {

        if(isInternetconnected){

            val datafromapi = apiService.getallads()
            if (datafromapi.success){

                return datafromapi.ads
            }

        }

        return listOf()

    }

    override suspend fun getallproductsbyCategory(category: String): List<Product> {
        return productDao.getallByCategory(category)
    }

    override suspend fun getProductbyID(productId: String): Product {
        return productDao.getProductbyID(productId)
    }
}