import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    id("root.publication")
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.jetbrainsCompose).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.dokka).apply(false)
}

@Suppress("UnstableApiUsage")
allprojects {
    group = "com.stevdza-san.messagebarkmp"
    version = "1.0.0"
    val sonatypeUsername = gradleLocalProperties(rootDir).getProperty("sonatypeUsername")
    val sonatypePassword = gradleLocalProperties(rootDir).getProperty("sonatypePassword")
    val gpgKeySecret = gradleLocalProperties(rootDir).getProperty("gpgKeySecret")
    val gpgKeyPassword = gradleLocalProperties(rootDir).getProperty("gpgKeyPassword")

    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    extensions.configure<PublishingExtension> {
        repositories {
            maven {
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = sonatypeUsername
                    password = sonatypePassword
                }
            }
        }

        val javadocJar = tasks.register<Jar>("javadocJar") {
            dependsOn(tasks.getByName<DokkaTask>("dokkaHtml"))
            archiveClassifier.set("javadoc")
            from("${layout.buildDirectory}/dokka")
        }

        publications {
            withType<MavenPublication> {
                artifact(javadocJar)
                pom {
                    groupId = "com.stevdza-san.messagebarkmp"
                    name.set("Message Bar KMP")
                    description.set("Animated Message Bar UI that can be wrapped around your composable content in order to display Error/Success messages in your app. Adapted to Material 3.")
                    licenses {
                        license {
                            name.set("Apache-2.0")
                            url.set("https://opensource.org/licenses/Apache-2.0")
                        }
                    }
                    url.set("stevdza-san.github.io/MessageBarKMP/")
                    issueManagement {
                        system.set("Github")
                        url.set("https://github.com/stevdza-san/MessageBarKMP")
                    }
                    scm {
                        connection.set("https://github.com/stevdza-san/MessageBarKMP.git")
                        url.set("https://github.com/stevdza-san/MessageBarKMP")
                    }
                    developers {
                        developer {
                            name.set("Stefan Jovanovic")
                            email.set("stefan.jovanavich@gmail.com")
                        }
                    }
                }
            }
        }
    }

    val publishing = extensions.getByType<PublishingExtension>()
    extensions.configure<SigningExtension> {
        useInMemoryPgpKeys(gpgKeySecret, gpgKeyPassword)
        sign(publishing.publications)
    }

    // TODO: remove after https://youtrack.jetbrains.com/issue/KT-46466 is fixed
    project.tasks.withType(AbstractPublishToMaven::class.java).configureEach {
        dependsOn(project.tasks.withType(Sign::class.java))
    }
}