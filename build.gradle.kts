// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0") // Use the latest version
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0") // Use the latest version
    }
}