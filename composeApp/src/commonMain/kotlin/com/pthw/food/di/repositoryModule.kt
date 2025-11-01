package com.pthw.food.di

import com.pthw.food.data.cache.mappers.FoodMapper
import com.pthw.food.data.cache.repository.CacheRepositoryImpl
import com.pthw.food.data.cache.repository.FoodRepositoryImpl
import com.pthw.food.domain.repository.CacheRepository
import com.pthw.food.domain.repository.FoodRepository
import org.koin.dsl.module

/**
 * Created by phyothihawin on 29/10/2025.
 */

val repositoryModule = module {
    factory<FoodMapper> { FoodMapper() }
    factory<FoodRepository> { FoodRepositoryImpl(get(), get()) }
    factory<CacheRepository> { CacheRepositoryImpl(get()) }
}