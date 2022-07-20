package com.dk.dksmtd.di

import com.chareem.core.coreModule
import com.test.test_karim2.Repository.GlobalRepositoryImpl
import com.test.test_karim2.Repository.globalRepository
import com.test.test_karim2.data.local.AppDatabase
import com.test.test_karim2.data.mapper.UserMapper
import com.test.test_karim2.data.remote.ApiService
import com.test.test_karim2.feature.main.AuthVM
import com.test.test_karim2.feature.main.fourth.FourthVM
import com.test.test_karim2.feature.main.third.ThirdVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val databaseModule = module {
    single { AppDatabase.getInstance(get()) }
}

val vmModule = module {
    viewModel { FourthVM(get()) }
    viewModel { AuthVM(get()) }
    viewModel { ThirdVM(get()) }
}

val mapperModule = module {
    single { UserMapper() }
}

val serviceModule = module {
    factory <ApiService> { get<Retrofit>().create(ApiService::class.java) }
}

val repositoriesModule = module {
    single <globalRepository>{ GlobalRepositoryImpl(get()) }
}

val mainModule = listOf(
    coreModule,
    databaseModule,
    vmModule,
    mapperModule,
    serviceModule,
    repositoriesModule
)