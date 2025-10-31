package com.pthw.food.di

import com.pthw.food.data.di.cacheModule
import com.pthw.food.ui.AppViewModel
import com.pthw.food.ui.home.HomePageViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/**
 * Created by phyothihawin on 29/10/2025.
 */

val appModule = module {
    factory {}
}

val viewModelsModule = module {
    viewModelOf(::HomePageViewModel)
}


fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(appModule, viewModelsModule, cacheModule, repositoryModule)
    }
