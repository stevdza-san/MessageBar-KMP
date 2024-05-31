import org.jetbrains.compose.ExperimentalComposeLibrary
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    id("com.vanniktech.maven.publish") version "0.28.0"
}

kotlin {
//    targetHierarchy.default()
    jvm("desktop")
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
        publishLibraryVariants("release", "debug")
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "messagebar"
            isStatic = true
        }
    }

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.androidx.activity.compose)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val iosArm64Main by getting {
            dependencies {}
        }

        val iosSimulatorArm64Main by getting {
            dependencies {}
        }

        val iosX64Main by getting {
            dependencies {}
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

android {
    namespace = "com.stevdza_san"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}


compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.stevdza-san.messagebarkmp"
            packageVersion = "1.0.2"
            description = "Message Bar KMP"
            copyright = "© 2024 Stevdza-San. All rights reserved."
        }
    }
}

mavenPublishing {
    coordinates(
        groupId = "com.stevdza-san",
        artifactId = "messagebarkmp",
        version = "1.0.2"
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("Message Bar KMP")
        description.set("Animated Message Bar UI that can be wrapped around your composable content in order to display Error/Success messages in your app. Adapted to Material 3.")
        inceptionYear.set("2024")
        url.set("https://github.com/stevdza-san/MessageBarKMP")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        // Specify developers information
        developers {
            developer {
                id.set("stevdza-san")
                name.set("Stefan Jovanovic")
                email.set("stefan.jovanavich@gmail.com")
            }
        }

        // Specify SCM information
        scm {
            url.set("https://github.com/stevdza-san/MessageBarKMP")
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()
}

task("testClasses")