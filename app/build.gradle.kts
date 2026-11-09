plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.legacy.kapt)
}

android {
    namespace = "com.dron.shoppinglist"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.dron.shoppinglist"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    buildFeatures {
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // Для поддержки корутин и Flow в Room
    // Компилятор Room (через KAPT, так как у вас legacy-kapt)
    kapt(libs.androidx.room.compiler)
    // Корутины
    implementation(libs.kotlinx.coroutines.android)
    // ViewModel + viewModelScope
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // LiveData + observe + Transformations
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // Lifecycle + lifecycleScope (если ещё нет)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}