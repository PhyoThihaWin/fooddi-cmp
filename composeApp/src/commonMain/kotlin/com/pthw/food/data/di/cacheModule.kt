package com.pthw.food.data.di

import org.koin.core.module.Module

const val DATABASE_NAME = "foodDi.db"
const val DATASTORE_FILENAME = "foodDi.preferences_pb"

expect val cacheModule: Module

