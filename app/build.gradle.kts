import org.gradle.internal.impldep.bsh.commands.dir

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.hrms_xyug"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hrms_xyug"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation ("com.google.android.material:material:1.9.0")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    testImplementation("junit:junit:4.13.2")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    implementation ("io.github.chaosleung:pinview:1.4.4")

    implementation ("androidx.appcompat:appcompat:1.5.1")
    implementation ("com.google.android.material:material:1.7.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.biometric:biometric:1.1.0")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.4")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation ("com.squareup.retrofit2:converter-gson:2.5.0")
    implementation ("com.squareup.retrofit2:retrofit:2.5.0")
  //  implementation ("com.squareup.okhttp3:logging-interceptor:4.2.1")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.5.0")

    // Glide dependency
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2") // For Java projects
    kapt ("com.github.bumptech.glide:compiler:4.14.2") // For Kotlin projects
    implementation ("id.zelory:compressor:3.0.1")
    implementation ("com.google.android.gms:play-services-maps:17.0.0")
    implementation ("com.google.android.gms:play-services-location:17.0.0")
    implementation ("com.airbnb.android:lottie:3.4.0")

//    implementation ("com.github.barteksc:android-pdf-viewer:2.8.2")
//    implementation ("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")

    implementation ("com.google.android.gms:play-services-auth-api-phone:18.0.1")
    implementation ("com.facebook.android:facebook-android-sdk:8.2.0")


    implementation ("com.squareup.picasso:picasso:2.5.0")
    implementation ("com.squareup.picasso:picasso:2.71828")

    implementation ("com.soundcloud.android:android-crop:1.0.1@aar")
    implementation ("com.github.bumptech.glide:glide:4.12.0")

    implementation ("androidx.work:work-runtime-ktx:2.8.0")

    implementation ("com.google.code.gson:gson:2.8.8")


    //about search view purpose
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("androidx.compose.material3:material3:1.2.0-alpha04")

    implementation("io.github.afreakyelf:Pdf-Viewer:2.1.1")

    implementation ("com.google.android.gms:play-services-auth:20.4.1")

    implementation ("com.google.firebase:firebase-messaging:20.1.0")
    implementation ("com.google.firebase:firebase-core:17.0.1")


//    dependencies {
        // AndroidX libraries
//        implementation("androidx.core:core-ktx:1.10.1")
//        implementation("androidx.appcompat:appcompat:1.7.0")
//        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//        implementation("androidx.biometric:biometric:1.1.0")
//        implementation("androidx.legacy:legacy-support-v4:1.0.0")
//        implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
//        implementation("androidx.test.ext:junit:1.2.1")
//        implementation("androidx.test.espresso:espresso-core:3.6.1")
//
//        // Google Play Services
//        implementation("com.google.android.gms:play-services-location:21.3.0")
//        implementation("com.google.android.gms:play-services-maps:17.0.0")
//        implementation("com.google.android.gms:play-services-auth-api-phone:18.0.1")
//        implementation("com.google.android.gms:play-services-auth:20.4.1")
//
//        // Firebase
//        implementation("com.google.firebase:firebase-messaging:20.1.0")
//        implementation("com.google.firebase:firebase-core:17.0.1")
//
//        // Libraries
//        implementation("com.github.bumptech.glide:glide:4.15.1")
//        kapt("com.github.bumptech.glide:compiler:4.15.1")
//        implementation("com.squareup.retrofit2:retrofit:2.5.0")
//        implementation("com.squareup.retrofit2:converter-gson:2.5.0")
//        implementation("com.squareup.retrofit2:converter-scalars:2.5.0")
//        implementation("com.squareup.okhttp3:logging-interceptor:4.2.1")
//        implementation("de.hdodenhof:circleimageview:3.1.0")
//        implementation("io.github.chaosleung:pinview:1.4.4")
//        implementation("io.github.afreakyelf:Pdf-Viewer:2.1.1")
//        implementation("com.facebook.android:facebook-android-sdk:8.2.0")
//        implementation("com.squareup.picasso:picasso:2.5.0")
//
//        // Lottie
//        implementation("com.airbnb.android:lottie:3.4.0")
//
//        // Search View and Compose
//        implementation("com.google.android.material:material:1.9.0")
//        implementation("androidx.compose.material3:material3:1.2.0-alpha04")
//    }

}
