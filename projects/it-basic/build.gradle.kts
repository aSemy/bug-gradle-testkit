plugins {
  kotlin("jvm") version "1.7.22"
  id("org.jetbrains.dokka") version "1.7.10"
}

buildscript {
  dependencies {
    classpath("org.jetbrains.dokka:dokka-base:${System.getenv("DOKKA_VERSION")}")
    //classpath("org.jetbrains.dokka:dokka-base:1.7.20")
  }
}

version = "1.7.20-SNAPSHOT"
