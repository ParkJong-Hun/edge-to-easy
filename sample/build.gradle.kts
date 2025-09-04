plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.spotless)
    alias(libs.plugins.kotlin.compose.compiler)
}

android {
    namespace = "io.github.parkjonghun.edgetoeasy.sample"
    compileSdk =
        libs.versions.compile.sdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "io.github.parkjonghun.edgetoeasy.sample"
        minSdk =
            libs.versions.min.sdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.target.sdk
                .get()
                .toInt()
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
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":edge-to-easy"))
    implementation(libs.bundles.androidx.app)
    implementation(libs.bundles.compose)
    implementation(platform(libs.androidx.compose.bom))

    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.bundles.compose.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
