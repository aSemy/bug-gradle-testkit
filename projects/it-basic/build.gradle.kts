plugins {
  kotlin("jvm") version "2.1.21"
  id("org.jetbrains.dokka") version "2.0.0"
}

buildscript {
  dependencies {
    classpath("org.jetbrains.dokka:dokka-base:${System.getenv("DOKKA_VERSION")}")
    //classpath("org.jetbrains.dokka:dokka-base:1.7.20")
  }
}

version = "1.7.20-SNAPSHOT"
