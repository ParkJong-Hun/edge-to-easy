plugins {
    id("maven-publish")
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.spotless)
}

group = "io.github.parkjonghun"
version = "0.0.1"

android {
    namespace = "io.github.parkjonghun.edgetoeasy.flow"
    compileSdk =
        libs.versions.compile.sdk
            .get()
            .toInt()

    defaultConfig {
        minSdk =
            libs.versions.min.sdk
                .get()
                .toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    testOptions {
        targetSdk =
            libs.versions.target.sdk
                .get()
                .toInt()
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

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(project(":edge-to-easy-core"))
    implementation(libs.bundles.androidx)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.android.test)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["release"])

                pom {
                    name.set("Edge to Easy Flow")
                    description.set("A Kotlin library that provides StateFlow and Flow for monitoring Insets changes in Android apps")
                    url.set("https://github.com/parkjonghun/edge-to-easy")
                    licenses {
                        license {
                            name.set("Apache License 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0")
                        }
                    }

                    developers {
                        developer {
                            id.set("parkjonghun")
                            name.set("Park Jong Hun")
                        }
                    }

                    scm {
                        connection.set("scm:git:git://github.com/parkjonghun/edge-to-easy.git")
                        developerConnection.set("scm:git:ssh://github.com:parkjonghun/edge-to-easy.git")
                        url.set("https://github.com/parkjonghun/edge-to-easy")
                    }
                }
            }
        }
    }
}
