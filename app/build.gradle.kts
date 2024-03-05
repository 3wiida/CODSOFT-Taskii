plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
    id ("kotlin-parcelize")
}

android {
    namespace = "com.mahmoudibrahem.taskii"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mahmoudibrahem.taskii"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.45")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")
    kapt("com.google.dagger:hilt-android-compiler:2.45")
    kapt ("androidx.hilt:hilt-compiler:1.0.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Room components
    implementation ("androidx.room:room-runtime:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")
    implementation("androidx.room:room-paging:2.5.2")

    // Paging 3.0
    implementation("androidx.paging:paging-compose:3.2.0")

    //Splash API
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Pager and Indicators - Accompanist
    implementation("com.google.accompanist:accompanist-pager:0.33.0-alpha")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.33.0-alpha")

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.0-alpha05")

    //System UI Controller
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")


    //Gson
    implementation("com.google.code.gson:gson:2.9.0")

    //Moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    kapt ("com.squareup.moshi:moshi-kotlin-codegen:1.8.0")

    //Date time picker
    implementation ("com.maxkeppeler.sheets-compose-dialogs:core:1.2.0")

    // CALENDAR
    implementation ("com.maxkeppeler.sheets-compose-dialogs:calendar:1.2.0")

    // CLOCK
    implementation ("com.maxkeppeler.sheets-compose-dialogs:clock:1.2.0")

    //Lottie
    implementation("com.airbnb.android:lottie-compose:6.0.1")
}