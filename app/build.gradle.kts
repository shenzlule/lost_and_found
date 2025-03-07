plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("androidx.room") version "2.6.1" apply false
}

android {
    namespace = "com.example.lost_and_found"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lost_and_found"
        minSdk = 28
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
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.storage)
    implementation(libs.androidx.cardview)
    implementation(libs.material)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    annotationProcessor(libs.androidx.room.compiler)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
// Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    implementation ("com.google.firebase:firebase-auth:23.2.0")
    implementation ("com.google.android.gms:play-services-auth:21.3.0")


    // Add Glide dependency for image loading
    implementation ("com.github.bumptech.glide:glide:4.12.0") // or the latest version
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")


    // AndroidX AppCompat (for Toolbar)
    implementation ("androidx.appcompat:appcompat:1.6.1")

    // Material Components (for TabLayout)
    implementation ("com.google.android.material:material:1.9.0")

    // ViewPager2 (for swipeable tabs)
    implementation ("androidx.viewpager2:viewpager2:1.0.0")




}