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
            ktlint(libs.versions.ktlint.get()).editorConfigOverride(mapOf(
                "ktlint_standard_no-unused-imports" to "disabled",
                "ktlint_standard_function-naming" to "disabled"
            ))
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

    // Configure Kotlin compiler options for all subprojects
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.addAll(listOf(
                "-opt-in=kotlin.RequiresOptIn"
            ))
        }
    }

    tasks.named("check") {
        dependsOn("spotlessCheck")
    }
}