plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.smartly.newapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.smartly.newapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources=true
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
        viewBinding=true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // retrofit
    implementation("io.reactivex.rxjava3:rxjava:3.1.9")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:adapter-rxjava3:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    // unit testing
    testImplementation("org.mockito:mockito-core:3.11.2")
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-inline:3.11.2")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.0")
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    // Coroutines test library
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.0")
    // LiveData test utilities
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    // lottie Animation
    implementation ("com.airbnb.android:lottie:6.6.0")
    //Glide Image
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation( "androidx.room:room-rxjava3:2.6.1") // Room RxJava integration

    implementation ("io.reactivex.rxjava3:rxjava:3.1.9")
    implementation ( "io.reactivex.rxjava3:rxandroid:3.0.2")

    implementation("androidx.room:room-runtime:2.6.1")
    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.cardview:cardview:1.0.0")

    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:2.6.1")
    //Kotlin Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.6.1")

    // optional - RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:2.6.1")

    // optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:2.6.1")

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    // sdp
    implementation("com.intuit.sdp:sdp-android:1.1.1")


    // Room KTX (for Kotlin Coroutines support)
    implementation ("androidx.room:room-ktx:2.6.1")
    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    //dagger Hilt
    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-android-compiler:2.52")
    // fragment Ktx
    implementation ("androidx.fragment:fragment-ktx:1.8.5")
    // navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.8.4")
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.4")
    //ssp
    implementation("com.intuit.ssp:ssp-android:1.0.6")
    //firebase

    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

    implementation ("com.google.firebase:firebase-messaging-ktx")
    implementation ("com.google.firebase:firebase-analytics-ktx")
    //OKHttp
    implementation ("com.squareup.okhttp3:logging-interceptor:4.5.0")
    //Cirecle
    implementation ("de.hdodenhof:circleimageview:3.1.0")



}
kapt{
    correctErrorTypes = true
}