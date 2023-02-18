package dev.adamko.dokkatoo

import dev.adamko.dokkatoo.utils.GradleProjectTest
import dev.adamko.dokkatoo.utils.GradleProjectTest.Companion.integrationTestProjectsDir
import dev.adamko.dokkatoo.utils.GradleProjectTest.Companion.projectTestTempDir
import dev.adamko.dokkatoo.utils.buildGradleKts
import dev.adamko.dokkatoo.utils.copyIntegrationTestProject
import dev.adamko.dokkatoo.utils.projectFile
import dev.adamko.dokkatoo.utils.settingsGradleKts
import dev.adamko.dokkatoo.utils.toTreeString
import dev.adamko.dokkatoo.utils.withEnvironment
import io.kotest.matchers.string.shouldContain
import java.io.File
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
        "--debug",
      )
      .forwardOutput()
      .withEnvironment(
        "DOKKA_VERSION" to "1.7.20",
      )
      .build()

    dokkaBuild.output shouldContain "BUILD SUCCESSFUL"
    dokkaBuild.output shouldContain "Generation completed successfully"

    val dokkaHtmlDir = dokkaProject.projectDir.resolve("build/dokka/html")

    val expectedFileTree = dokkaHtmlDir.toTreeString()
    println(expectedFileTree)
  }
}

private fun initDokkaProject(
  destinationDir: File,
): GradleProjectTest {
  return GradleProjectTest(destinationDir.toPath()).apply {
    copyIntegrationTestProject("it-basic/dokka")

    buildGradleKts = buildGradleKts
      .replace(
        """kotlin("jvm")""",
        """kotlin("jvm") version "1.7.22"""",
      )

    var templateGradleSettings: String by projectFile("template.settings.gradle.kts")
    templateGradleSettings = templateGradleSettings
      .replace("for-integration-tests-SNAPSHOT", "1.7.20")
  }
}
