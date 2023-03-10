package dev.adamko.dokkatoo


import dev.adamko.dokkatoo.GradleProjectTest.Companion.integrationTestProjectsDir
import dev.adamko.dokkatoo.GradleProjectTest.Companion.projectTestTempDir
import io.kotest.matchers.string.shouldContain
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.properties.ReadOnlyProperty
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test

/**
 * Integration test for the `it-basic` project in Dokka
 *
 * Runs Dokka & Dokkatoo, and compares the resulting HTML site.
 */
class BasicProjectIntegrationTest {

  @Test
  fun `test basic project`() {

    val basicProjectSrcDir = integrationTestProjectsDir.resolve("it-basic")

    val tempDir = projectTestTempDir.resolve("it/it-basic").toFile()

    val dokkaDir = tempDir.resolve("dokka")
    basicProjectSrcDir.toFile()
      .copyRecursively(dokkaDir, overwrite = true) { _, _ -> OnErrorAction.SKIP }

    val dokkaProject = initDokkaProject(dokkaDir)

    val dokkaBuild = dokkaProject.runner
      .withArguments(
        "clean",
        "dokkaHtml",
        "--stacktrace",
        "--info",
      )
      .forwardOutput()
      //.withEnvironment(
      //  mapOf(
      //    "DOKKA_VERSION" to "1.7.20",
      //  )
      //)
      .withEnvironment(
        mapOf(
          "DOKKA_VERSION" to "1.7.20",
        )
      )
      .build()

    dokkaBuild.output shouldContain "BUILD SUCCESSFUL"
    dokkaBuild.output shouldContain "Generation completed successfully"
  }
}

private fun initDokkaProject(
  destinationDir: File,
): GradleProjectTest {
  return GradleProjectTest(destinationDir.toPath()).apply {
    integrationTestProjectsDir
      .resolve("it-basic/dokka")
      .toFile()
      .copyRecursively(projectDir.toFile(), overwrite = true) { _, _ -> OnErrorAction.SKIP }
  }
}

class GradleProjectTest(
  val projectDir: Path,
) {

  val runner: GradleRunner = GradleRunner.create().withProjectDir(projectDir.toFile())

  companion object {

    val projectTestTempDir: Path by systemProperty(Paths::get)

    /** Dokka Source directory that contains Gradle projects used for integration tests */
    val integrationTestProjectsDir: Path by systemProperty(Paths::get)

    private fun <T> systemProperty(
      convert: (String) -> T,
    ) = ReadOnlyProperty<Any, T> { _, property ->
      val value = requireNotNull(System.getProperty(property.name)) {
        "system property ${property.name} is unavailable"
      }
      convert(value)
    }
  }
}
