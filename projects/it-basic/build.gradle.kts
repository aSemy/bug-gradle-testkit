import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.kotlinSourceSet
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.DokkaConfiguration
import java.net.URL

plugins {
    kotlin("jvm") version "1.7.22"
    id("org.jetbrains.dokka") version "1.7.10"
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:${System.getenv("DOKKA_VERSION")}")
    }
}

version = "1.7.20-SNAPSHOT"


repositories {
    maven("https://cache-redirector.jetbrains.com/jcenter.bintray.com")
    mavenLocal()
    mavenCentral()
    google()
    maven("https://cache-redirector.jetbrains.com/dl.bintray.com/kotlin/kotlin-eap")
    maven("https://cache-redirector.jetbrains.com/dl.bintray.com/kotlin/kotlin-dev")
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") {
        content {
            includeGroup("org.jetbrains.kotlinx")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test-junit"))
}

tasks.withType<DokkaTask> {
    moduleName.set("Basic Project")
    dokkaSourceSets {
        configureEach {
            documentedVisibilities.set(
                setOf(DokkaConfiguration.Visibility.PUBLIC, DokkaConfiguration.Visibility.PROTECTED)
            )
            suppressedFiles.from(file("src/main/kotlin/it/suppressedByPath"))
            perPackageOption {
                matchingRegex.set("it.suppressedByPackage.*")
                suppress.set(true)
            }
            perPackageOption {
                matchingRegex.set("it.overriddenVisibility.*")
                documentedVisibilities.set(
                    setOf(DokkaConfiguration.Visibility.PRIVATE)
                )
            }
            sourceLink {
                localDirectory.set(file("src/main"))
                remoteUrl.set(
                    URL(
                        "https://github.com/Kotlin/dokka/tree/master/" +
                                "integration-tests/gradle/projects/it-basic/src/main"
                    )
                )
            }
        }

        register("myTest") {
            kotlinSourceSet(kotlin.sourceSets["test"])
        }
    }
    suppressObviousFunctions.set(false)

    pluginsMapConfiguration.set(mapOf(DokkaBase::class.qualifiedName to """{ "customStyleSheets": ["${file("./customResources/logo-styles.css").invariantSeparatorsPath}", "${file("./customResources/custom-style-to-add.css").invariantSeparatorsPath}"], "customAssets" : ["${file("./customResources/custom-resource.svg").invariantSeparatorsPath}"] }"""))
}
