import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("maven-publish")
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.spotless)
}

group = "io.github.parkjonghun"
version = "0.0.1"

android {
    namespace = "io.github.parkjonghun.edgetoeasy.core"
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

    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.add("-Xexplicit-api=warning")
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.bundles.androidx)
    implementation(libs.androidx.recyclerview)

    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.android.test)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["release"])

                groupId = "io.github.parkjonghun"
                artifactId = "edge-to-easy-core"
                version = project.version.toString()

                pom {
                    name.set("Edge to Easy Core")
                    description.set("A Kotlin library that makes it easier to enable Edge to Edge in your Android app")
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

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/parkjonghun/edge-to-easy")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
                }
            }
        }
    }
}
