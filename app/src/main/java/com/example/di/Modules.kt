package com.example.di


import android.content.Context
import androidx.room.Room
import com.example.main.MainViewModel
import com.example.model.db.AppDatabase
import com.example.model.net.createApiService
import com.example.model.repository.ProductRepository
import com.example.model.repository.ProductRepositoryIMPL
import com.example.model.repository.UserRepository
import com.example.model.repository.UserRepositoryIMPL
import com.example.signin.SignInviewmodel
import com.example.signup.SignUpviewmodel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mymodules = module {

    single { createApiService() }
    single { androidContext().getSharedPreferences("data", Context.MODE_PRIVATE) }
    single {
        Room.databaseBuilder(androidContext(),AppDatabase::class.java,"app_dataBase.db").allowMainThreadQueries().build()
    }
    single<ProductRepository> {ProductRepositoryIMPL(
        get(),
        get<AppDatabase>().productDao()

    )


    }

    single<UserRepository> { UserRepositoryIMPL(get(),get()) }

    viewModel {SignUpviewmodel(get())}
    viewModel { SignInviewmodel(get()) }

    viewModel { (isnetconnected:Boolean) -> MainViewModel(get(),isnetconnected) }



}