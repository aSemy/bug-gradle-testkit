rootProject.name = "testkit-windows"

pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

@Suppress("UnstableApiUsage") // Central declaration of repositories is an incubating feature
dependencyResolutionManagement {

  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

  repositories {
    mavenCentral()
    google()

    maven("https://www.jetbrains.com/intellij-repository/snapshots")
    maven("https://www.jetbrains.com/intellij-repository/releases")
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-ide")
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-ide-plugin-dependencies")
    maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
    maven("https://www.myget.org/F/rd-snapshots/maven/")

    ivy("https://github.com/") {
      name = "GitHub Release"
      patternLayout {
        artifact("[organization]/[module]/archive/[revision].[ext]")
        artifact("[organization]/[module]/archive/refs/tags/[revision].[ext]")
        artifact("[organization]/[module]/archive/refs/tags/v[revision].[ext]")
      }
      metadataSources { artifact() }
    }
  }
}

plugins {
  id("com.gradle.enterprise") version "3.12.3"
}

gradleEnterprise {

  buildScan {
    val isCI = providers.environmentVariable("CI").orNull.toBoolean()

    tag(if (isCI) "CI" else "local")
    tag(providers.systemProperty("os.name").orNull)
    tag(providers.systemProperty("os.arch").orNull)

    if (isCI) {
      // only automatically enable build scan on CI
      termsOfServiceUrl = "https://gradle.com/terms-of-service"
      termsOfServiceAgree = "yes"
      publishAlways()
      isUploadInBackground = false

      val ghServer = providers.environmentVariable("GITHUB_SERVER_URL").orNull
      val ghRepo = providers.environmentVariable("GITHUB_REPOSITORY").orNull
      val giRunId = providers.environmentVariable("GITHUB_RUN_ID").orNull
      link("GitHub Workflow run", "$ghServer/$ghRepo/actions/runs/$giRunId")
    }
  }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
