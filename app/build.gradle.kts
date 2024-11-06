plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("com.chaquo.python")
}

android {
    namespace = "com.temple.aldwairi_projects_emotionecho"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.temple.aldwairi_projects_emotionecho"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        //The Python interpreter is a native component,
        // so you must use the abiFilters setting to specify which ABIs you want the app to support
        ndk {
            // On Apple silicon, you can omit x86_64.
            abiFilters += listOf("arm64-v8a", "x86_64")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    flavorDimensions += "pyVersion"
    productFlavors {
        create("py311") { dimension = "pyVersion" }
    }
}

//used for changing Chaquopy plugin settings
chaquopy {
    defaultConfig {
        pip {
            install("numpy")
        }
    }
    productFlavors {
        getByName("py311") { version = "3.11" }
    }
    sourceSets { }
}

dependencies {//Android
    implementation(libs.androidx.core.ktx.v1131)
    implementation(libs.androidx.lifecycle.runtime.ktx.v286)

    /** Compose **/
    implementation(platform(libs.androidx.compose.bom.v20240903))
    implementation(libs.androidx.activity.compose.v192)
    //noinspection UseTomlInstead
    implementation("androidx.compose.ui:ui")
    //noinspection UseTomlInstead
    implementation("androidx.compose.ui:ui-graphics")
    //noinspection UseTomlInstead
    implementation("androidx.compose.ui:ui-tooling-preview")
    //noinspection UseTomlInstead
    implementation("androidx.compose.material3:material3")
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.material)
    //noinspection UseTomlInstead
    implementation("androidx.compose.foundation:foundation")
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.play.services.location)
    androidTestImplementation(platform(libs.androidx.compose.bom.v20240903))
    //noinspection UseTomlInstead
    debugImplementation("androidx.compose.ui:ui-tooling")
    //noinspection UseTomlInstead
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //for navigating through screens in Compose
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    /** Testing **/
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //noinspection UseTomlInstead
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")


    /** Firebase **/
    //Firebase Auth
    implementation(libs.firebase.auth)

    //Firebase Firestore
    implementation(libs.firebase.firestore)
    // Firebase Storage
    implementation(platform(libs.firebase.bom))
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-storage")
    implementation(libs.firebase.analytics)

    /** GSon **/
    implementation(libs.gson)

    /** Rooms **/
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)

    /** Retrofit **/
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    implementation (libs.accompanist.swiperefresh)

    /** Test rules and transitive dependencies: **/
    //noinspection UseTomlInstead
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    // Needed for createComposeRule(), but not for createAndroidComposeRule<YourActivity>():
    //noinspection UseTomlInstead
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}