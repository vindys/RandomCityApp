plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    id("com.google.devtools.ksp")
    id ("com.google.dagger.hilt.android") // Apply the plugin
}

android {
    namespace = "com.example.randomcityapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.randomcityapp"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    lint {
        abortOnError = true
        warningsAsErrors = true
        checkReleaseBuilds = true
        xmlReport = true
        htmlReport = true
        lintConfig = file("lint.xml")
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.androidx.material.icons)

    // Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.compose)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    //retrofit coroutines
    implementation(libs.retrofit.coroutine.adapter)
    //moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.kotlin.reflect)

    // Data store
    implementation(libs.datastore)
    implementation(libs.datastore.preferences)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    // Test & Debug
    testImplementation(libs.junit)
    testImplementation(libs.dagger.hilt.testing)
    // Fo mocking and fake instances/values
    testImplementation (libs.mockk)
    testImplementation(libs.mokito)
    testImplementation(libs.mokito.kotlin)
    testImplementation(libs.mokito.inline)
    // Coroutine test rules
    testImplementation(libs.coroutine.testing)
    testImplementation(libs.kotlinx.coroutines.test.v173)
    //turbine for testing states in mvi
    testImplementation(libs.turbine) // for testing Flows


    // Room
    testImplementation(libs.room.testing)
    androidTestImplementation(libs.dagger.hilt.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    debugImplementation(libs.compose.ui.testing)
    // Needed for createAndroidComposeRule, but not createComposeRule:
    debugImplementation(libs.compose.testing.manifest)
    //kaptAndroidTest(libs.dagger.hilt.compiler)
    kaptAndroidTest(libs.dagger.hilt.android.compiler)
    kaptTest(libs.dagger.hilt.compiler)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(kotlin("test"))


}

//class RoomSchemaArgProvider(
//    @get:InputDirectory
//    @get:PathSensitive(PathSensitivity.RELATIVE)
//    val schemaDir: File
//) : CommandLineArgumentProvider {
//
//    override fun asArguments(): Iterable<String> {
//        return listOf("room.schemaLocation=${schemaDir.path}")
//    }
//}
//
//ksp {
//    arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
//}