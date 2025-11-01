package com.pthw.food.data.cache.repository

import com.pthw.food.data.cache.database.AppDatabase
import com.pthw.food.data.cache.mappers.FoodMapper
import com.pthw.food.domain.repository.FoodRepository

/**
 * Created by P.T.H.W on 19/07/2024.
 */
class FoodRepositoryImpl(
    private val database: AppDatabase,
    private val mapper: FoodMapper
) : FoodRepository {
    override suspend fun getAllFood() = database.foodDao().getAllFood().map(mapper::map)

    override suspend fun getSearchFood(word: String) = database.foodDao().getSearchFood(word).map(mapper::map)

    override suspend fun getFoodByType(type: String) = database.foodDao().getFoodByType(type).map(mapper::map)
}