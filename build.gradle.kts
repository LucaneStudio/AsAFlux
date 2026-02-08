plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

// Force la version Kotlin 2.3.10 dans tous les sous-projets
// pour éviter les conflits de version avec les dépendances transitives
subprojects {
    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-stdlib:2.3.10")
            force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.3.10")
            force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.3.10")
        }
    }
}