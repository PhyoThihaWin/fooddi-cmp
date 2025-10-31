package com.pthw.food.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.pthw.food.data.cache.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Created by phyothihawin on 29/10/2025.
 */

actual val cacheModule: Module = module {
    single<AppDatabase> {
        getDatabaseBuilder(get())
    }

    single<DataStore<Preferences>> {
        getDataStore(get())
    }
}

fun getDatabaseBuilder(ctx: Context): AppDatabase {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
        .createFromAsset("database/fooddi.db")
        .fallbackToDestructiveMigration()
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

fun getDataStore(ctx: Context): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { ctx.filesDir.resolve(DATASTORE_FILENAME).absolutePath.toPath() }
    )
}
