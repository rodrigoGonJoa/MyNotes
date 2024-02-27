// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt.gradle) apply false
    alias(libs.plugins.ksp) apply false
    //./gradlew detekt    ./gradlew detektGenerateConfig
    alias(libs.plugins.detekt)
    //./gradlew buildHealth
    alias(libs.plugins.dependency.analysis)
}
true // Needed to make the Suppress annotation work for the plugins block