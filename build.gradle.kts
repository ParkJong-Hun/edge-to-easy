plugins {
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.spotless) apply false
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            licenseHeaderFile(rootProject.file("license-header.kt"))
            ktlint(libs.versions.ktlint.get())
            trimTrailingWhitespace()
            leadingTabsToSpaces(4)
            endWithNewline()
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint(libs.versions.ktlint.get())
            trimTrailingWhitespace()
            leadingTabsToSpaces(4)
            endWithNewline()
        }
    }

    tasks.named("check") {
        dependsOn("spotlessCheck")
    }
}