import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    id("com.vanniktech.maven.publish") version "0.31.0"
}

kotlin {
    jvm("desktop")
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
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
            linkerOpts.add("-Xbinary=bundleId=com.stevdza_san.messagebar")
        }
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
        iosMain.dependencies {}
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)

            implementation(libs.compose.icons)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        wasmJsMain.dependencies {}
    }
}

android {
    namespace = "com.stevdza_san"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}


compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.stevdza-san.messagebarkmp"
            packageVersion = "1.0.8"
            description = "Message Bar KMP"
            copyright = "© 2024 Stevdza-San. All rights reserved."
        }
    }
}

mavenPublishing {
    coordinates(
        groupId = "com.stevdza-san",
        artifactId = "messagebarkmp",
        version = "1.0.8"
    )

    // Configure POM metadata for the published artifact
    pom {
        name.set("Message Bar KMP")
        description.set("Animated Message Bar UI that can be wrapped around your composable content in order to display Error/Success messages in your app. Adapted to Material 3.")
        inceptionYear.set("2024")
        url.set("https://github.com/stevdza-san/MessageBar-KMP")

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
            url.set("https://github.com/stevdza-san/MessageBar-KMP")
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()
}