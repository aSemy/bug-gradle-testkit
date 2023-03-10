@file:Suppress("LocalVariableName", "UnstableApiUsage")

rootProject.name = "it-basic"

pluginManagement {
  repositories {
    //mavenLocal()
    maven("https://cache-redirector.jetbrains.com/jcenter.bintray.com")
    mavenCentral()
    gradlePluginPortal()
    google()
    maven("https://cache-redirector.jetbrains.com/dl.bintray.com/kotlin/kotlin-eap")
    maven("https://cache-redirector.jetbrains.com/dl.bintray.com/kotlin/kotlin-dev")
  }
}
