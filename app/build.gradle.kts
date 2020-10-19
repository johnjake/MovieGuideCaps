import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")

    defaultConfig {
        applicationId = "ph.movieguide.com"
        minSdkVersion(26)
        targetSdkVersion(29)
        versionCode = 4163
        versionName = "0.9.9"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    buildTypes {

        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".dev"
            getProps(rootProject.file("debug.properties")).forEach { prop ->
                buildConfigField("String", prop.key.toString(), prop.value.toString())
            }
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = false
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    (kotlinOptions as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions).apply {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {

    val daggerVersion = "2.29.1"
    val coroutineCore = "1.3.7"
    val coroutineAndroid = "1.3.6"
    val lifecycleVersion = "2.2.0"
    val cardViewVersion = "1.0.0"
    val archVersion = "2.1.0"
    val retrofitVersion = "2.9.0"
    val supportMaterialDesignVersion = "1.3.0-alpha03"
    val converterScalars = "2.9.0"
    val okhttpVersion = "4.9.0"
    val okhttpLoggingVersion = "4.9.0"
    val rxJava2Version = "2.2.10"
    val rxKotlinVersion = "2.3.0"
    val rxAndroidVersion = "2.1.1"
    val rxBindingXVersion = "3.1.0"
    val rxMathVersion = "0.20.10"
    val timberVersion = "4.7.1"
    val ktxCore = "1.3.2"
    val fragmentKtx = "1.2.5"
    val sharedPreferencesVersion  = "1.1.1"
    val archCompVersion = "2.3.0-alpha03"
    val lottieVersion = "3.4.4"
    val coilVersion = "0.13.0"
    val pagingVersion  = "3.0.0-alpha07"
    val constrainLayoutVersion = "2.0.2"
    val roundImageVersion = "2.3.0"
    val navFragmentKtxVersion  = "2.3.1"
    val navUiKtxVersion = "2.3.1"
    val androidAnnotation = "28.0.0"
    val androidxAnnotation = "1.1.0"

    implementation(
        fileTree(
            mapOf(
                "include" to listOf("*.jar"),
                "dir" to "libs"
            )
        )
    )

    implementation(project(mapOf("path" to ":android-baseplate-persistence")))

    implementation("com.android.support:support-annotations:$androidAnnotation")
    implementation("androidx.annotation:annotation:$androidxAnnotation")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.10")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.2")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")

    //Dagger
    api("com.google.dagger:dagger-android:$daggerVersion")
    api("com.google.dagger:dagger-android-support:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")

    implementation ("org.koin:koin-android:2.0.1")
    implementation ("org.koin:koin-androidx-viewmodel:2.0.1")
    implementation ("org.koin:koin-androidx-scope:2.0.1")

    //httpOK
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpLoggingVersion")

    //CircleIndicator
    implementation ("me.relex:circleindicator:2.1.4")

    //sharedPreferrences
    implementation ("androidx.preference:preference-ktx:$sharedPreferencesVersion")

    //room
    //implementation ("androidx.room:room-coroutines:2.1.0-alpha04")
    implementation ("androidx.room:room-runtime:$archCompVersion")
    implementation ("androidx.room:room-ktx:$archCompVersion")
    kapt ("androidx.room:room-compiler:$archCompVersion")

    //timber
    implementation("com.jakewharton.timber:timber:$timberVersion")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-scalars:$converterScalars")

    //design
    implementation("androidx.cardview:cardview:$cardViewVersion")
    implementation("com.google.android.material:material:$supportMaterialDesignVersion")
    implementation ("com.android.support.constraint:constraint-layout:$constrainLayoutVersion")
    implementation ("com.makeramen:roundedimageview:$roundImageVersion")
    implementation ("com.airbnb.android:lottie:$lottieVersion")
    implementation ("com.android.support:design:29.0.0")

    //rxJava
    implementation("io.reactivex.rxjava2:rxjava:$rxJava2Version")
    implementation("io.reactivex.rxjava2:rxkotlin:$rxKotlinVersion")
    implementation("io.reactivex.rxjava2:rxandroid:$rxAndroidVersion")
    implementation("com.github.akarnokd:rxjava2-extensions:$rxMathVersion")

    implementation("com.jakewharton.rxbinding3:rxbinding:$rxBindingXVersion")
    implementation("com.jakewharton.rxbinding3:rxbinding-core:$rxBindingXVersion")
    implementation("com.jakewharton.rxbinding3:rxbinding-appcompat:$rxBindingXVersion")

    //fragment-KTX
    implementation ("androidx.core:core-ktx:$ktxCore")
    implementation ("androidx.fragment:fragment-ktx:$fragmentKtx")

    implementation("androidx.navigation:navigation-fragment-ktx:$navFragmentKtxVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navUiKtxVersion")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //LifeCycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-service:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-process:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycleVersion")
    testImplementation("androidx.arch.core:core-testing:$archVersion")
    kapt("androidx.lifecycle:lifecycle-compiler:2.2.0")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineCore")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineAndroid")

    //image viewer
    implementation("io.coil-kt:coil:$coilVersion")

    //paging
    implementation ("androidx.paging:paging-runtime:$pagingVersion") // For Kotlin use paging-runtime-ktx

    testImplementation("junit:junit:4.13.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}

fun Project.android(configure: com.android.build.gradle.internal.dsl.BaseAppModuleExtension.() -> Unit) {
    return (this as ExtensionAware).extensions.configure(
        "android",
        configure
    )
}

fun getProps(file: File): java.util.Properties {
    val props = Properties()
    props.load(file.inputStream())
    return props
}
