@file:Suppress("UnstableApiUsage") // jvm test suites & test report aggregation are incubating

import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version embeddedKotlinVersion
  kotlin("plugin.serialization") version embeddedKotlinVersion
}

dependencies {
  testImplementation(gradleTestKit())
  testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$embeddedKotlinVersion")

  testImplementation(platform("io.kotest:kotest-bom:5.5.5"))
  testImplementation("io.kotest:kotest-assertions-core")
  testImplementation("org.jetbrains.dokka:dokka-core:1.7.20")
}

tasks.withType<Test>().configureEach {
  useJUnitPlatform()

  testLogging {
    events = setOf(
      TestLogEvent.STARTED,
      TestLogEvent.PASSED,
      TestLogEvent.SKIPPED,
      TestLogEvent.FAILED,
      TestLogEvent.STANDARD_OUT,
      TestLogEvent.STANDARD_ERROR,
    )
    showStandardStreams = true
    showExceptions = true
    showCauses = true
    showStackTraces = true
  }

  val projectTestTempDirPath = "$buildDir/test-temp-dir"
  inputs.property("projectTestTempDir", projectTestTempDirPath)
  systemProperty("projectTestTempDir", projectTestTempDirPath)

  systemProperty("integrationTestProjectsDir", "$projectDir/projects")
}
