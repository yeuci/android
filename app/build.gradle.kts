plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

//    id("com.android.application")
//    id("org.jetbrains.kotlin.android")
//    id("org.jetbrains.kotlin.plugin.serialization")

}

android {
    namespace = "com.example.group_project"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.group_project"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}