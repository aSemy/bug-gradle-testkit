@file:Suppress("UnstableApiUsage") // jvm test suites & test report aggregation are incubating

import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") //version embeddedKotlinVersion
  kotlin("plugin.serialization") version embeddedKotlinVersion
  `java-test-fixtures`

  `jvm-test-suite`
  `test-report-aggregation`
}

dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

  testFixturesImplementation(gradleTestKit())
  testFixturesImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
  testFixturesImplementation(platform("io.kotest:kotest-bom:5.5.5"))
  testFixturesImplementation("io.kotest:kotest-runner-junit5")
  testFixturesImplementation("io.kotest:kotest-assertions-core")
  testFixturesImplementation("io.kotest:kotest-assertions-json")

//  kotlinDokkaSource(projects.externals)

  // don't define test dependencies here, instead define them in the testing.suites {} configuration below
}


tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    this.freeCompilerArgs += listOf(
      "-opt-in=kotlin.RequiresOptIn",
      //"-opt-in=dev.adamko.dokkatoo.internal.DokkatooInternalApi",
    )
  }
}

testing.suites {

  withType<JvmTestSuite>().configureEach {
    useJUnitJupiter()

    dependencies {
      implementation(project.dependencies.gradleTestKit())

      implementation("org.jetbrains.kotlin:kotlin-test:1.7.20")

      implementation(project.dependencies.platform("io.kotest:kotest-bom:5.5.5"))
      implementation("io.kotest:kotest-runner-junit5")
      implementation("io.kotest:kotest-assertions-core")
      implementation("io.kotest:kotest-assertions-json")

      implementation(project.dependencies.testFixtures(project()))

      implementation("org.jetbrains.dokka:dokka-core:1.7.20")
      implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    }

    targets.configureEach {
      testTask.configure {
        val projectTestTempDirPath = "$buildDir/test-temp-dir"
        inputs.property("projectTestTempDir", projectTestTempDirPath)
        systemProperty("projectTestTempDir", projectTestTempDirPath)

        systemProperty("integrationTestProjectsDir", "$projectDir/projects")
      }
    }
  }

  /** Integration tests suite */
  val testIntegration by registering(JvmTestSuite::class)

  tasks.check { dependsOn(testIntegration) }
}


tasks.withType<Test>().configureEach {

  mustRunAfter(tasks.withType<AbstractPublishToMaven>())

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
}

tasks.withType<Test>().configureEach {
  // this seems to help OOM errors in the Worker Daemons
  setForkEvery(1)
  jvmArgs(
    "-Xmx1g",
    "-XX:MaxMetaspaceSize=512m",
  )
}
