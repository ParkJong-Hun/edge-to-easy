rootProject.name = "edge-to-easy"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
        maven {
            url = uri("https://androidx.dev/snapshots/builds/13508953/artifacts/repository")
        }
    }
}

include(":edge-to-easy-core")
include(":edge-to-easy-flow")
include(":sample")