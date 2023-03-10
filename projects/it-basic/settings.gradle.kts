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

@Suppress("UnstableApiUsage") // Central declaration of repositories is an incubating feature
dependencyResolutionManagement {

  repositories {
    mavenCentral()
    google()

    maven("https://www.jetbrains.com/intellij-repository/snapshots")
    maven("https://www.jetbrains.com/intellij-repository/releases")
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-ide")
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-ide-plugin-dependencies")
    maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
    maven("https://www.myget.org/F/rd-snapshots/maven/")
  }
}
