package com.pthw.food.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.pthw.food.data.cache.database.AppDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import okio.Path.Companion.toPath
import org.koin.dsl.module
import platform.Foundation.NSBundle
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/**
 * Created by phyothihawin on 29/10/2025.
 */

actual val cacheModule = module {
    single<AppDatabase> {
        getDatabaseBuilder()
    }

    single<DataStore<Preferences>> {
        getDataStore()
    }
}

fun getDatabaseBuilder(): AppDatabase {
    copyPrepopulatedDb()
//    debugBundleContents()

    val dbFilePath = documentDirectory() + "/${DATABASE_NAME}"
    println("Database file path: $dbFilePath")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFilePath
    )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

@OptIn(ExperimentalForeignApi::class)
fun copyPrepopulatedDb() {
    // Locate the prepopulated DB file in the iOS bundle
    val bundlePath = NSBundle.mainBundle.pathForResource("compose-resources/fooddi", "db")
        ?: error("Prepopulated DB not found in bundle!")

    // Copy it to the documents directory (since Room can’t open directly from bundle)
    val fileManager = NSFileManager.defaultManager
    val documentsDir = NSSearchPathForDirectoriesInDomains(
        NSDocumentDirectory, NSUserDomainMask, true
    ).firstOrNull() as? String ?: error("Documents directory not found")

    val destinationPath = "$documentsDir/$DATABASE_NAME"

    if (!fileManager.fileExistsAtPath(destinationPath)) {
        fileManager.copyItemAtPath(bundlePath, destinationPath, null)
    } else {
        println("Prepopulated DB already exists at $destinationPath")
    }
}

private fun debugBundleContents() {
    val bundle = NSBundle.mainBundle
    val resources = bundle.pathsForResourcesOfType(null, null)
    println("=== Bundle contains ${resources.size} files ===")
    resources.forEach { println(it) }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}

@OptIn(ExperimentalForeignApi::class)
fun getDataStore(): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            (requireNotNull(documentDirectory).path + "/${DATASTORE_FILENAME}").toPath()
        })
}