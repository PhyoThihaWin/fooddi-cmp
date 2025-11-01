import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.crashlytics)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            export("io.github.mirzemehdi:kmpnotifier:1.6.0")
        }
    }

    cocoapods {
        version = "1.0"
        summary = "CocoaPods library"
        homepage = "https://github.com/JetBrains/kotlin"
        ios.deploymentTarget = "16.0"

        framework {
            baseName = "composeApp"
            isStatic = true
        }

        pod("FBAudienceNetwork") {
            version = "6.21.0"
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.ktor.client.okhttp)

            implementation("androidx.core:core-splashscreen:1.0.1")

            implementation("androidx.annotation:annotation:1.9.1")
            implementation("com.facebook.android:audience-network-sdk:6.21.0") {
                exclude("com.android.support", "support-compat")
            }

            implementation(project.dependencies.platform(libs.android.firebase.bom))
            implementation(libs.android.firebase.analytics)
            implementation(libs.android.firebase.crashlytics)
            implementation(libs.android.firebase.messaging)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("org.jetbrains.androidx.savedstate:savedstate-compose:1.3.5")
            implementation("tech.annexflow.compose:constraintlayout-compose-multiplatform:0.6.1")
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.compottie)

            api(project.dependencies.platform(libs.koin.bom))
            api(libs.koin.core)
            api(libs.koin.compose)
            api(libs.koin.compose.viewmodel)

            implementation("androidx.datastore:datastore-preferences:1.2.0-beta01")
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            api("io.github.mirzemehdi:kmpnotifier:1.6.0")
            implementation("dev.icerock.moko:permissions-compose:0.20.1")
            implementation("dev.icerock.moko:permissions-notifications:0.20.1")
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.pthw.food"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.pthw.food"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        buildConfig = true
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    debugImplementation(compose.uiTooling)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

