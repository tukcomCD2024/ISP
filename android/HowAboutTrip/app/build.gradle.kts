import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {
    namespace = "com.project.how"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.project.how"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildFeatures{
            dataBinding = true
            buildConfig = true
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue("string", "API_SERVER", getApiKey("api_server"))
        resValue("string", "GOOGLE_AD_MOB_ID", getApiKey("google_ad_mob_id"))
        resValue("string", "GOOGLE_AD_MOB_UNIT_ID", getApiKey("google_ad_mob_unit_id"))
        resValue("string", "GOOGLE_AD_MOB_BANNER_TEST", getApiKey("google_ad_mob_banner_test"))
        resValue("string", "GOOGLE_MAP_API_KEY", getApiKey("google_map_api_key"))
        resValue("string", "GOOGLE_PLACE_API_KEY", getApiKey("google_place_api_key"))

        buildConfigField("String", "ERROR_IMAGE_URl", getApiKey("error_image_url"))
        buildConfigField("String", "GOOGLE_OAUTH_ID", getApiKey("google_oauth_id"))
        buildConfigField("String", "GOOGLE_MAP_API_KEY", getApiKey("google_map_api_key"))
        buildConfigField("String", "GOOGLE_PLACE_API_KEY", getApiKey("google_place_api_key"))
        buildConfigField("String", "GOOGLE_SERVER_ID", getApiKey("google_server_id"))
        buildConfigField("String", "API_SERVER", getApiKey("api_server"))
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
}

fun getApiKey(propertyKey: String):String = gradleLocalProperties(rootDir, providers).getProperty(propertyKey)

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.google.android.gms:play-services-ads:23.0.0")
    implementation("com.airbnb.android:lottie:6.4.0")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.fragment:fragment-ktx:1.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    implementation("com.squareup.retrofit:retrofit:2.0.0-beta2")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.11.0")

    implementation("com.github.bumptech.glide:glide:5.0.0-rc01")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.libraries.places:places:3.4.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.activity:activity:1.9.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}