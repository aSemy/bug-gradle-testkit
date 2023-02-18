package dev.adamko.dokkatoo.utils

import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import org.gradle.testkit.runner.GradleRunner
import org.intellij.lang.annotations.Language


// utils for testing using Gradle TestKit


class GradleProjectTest(
  override val projectDir: Path,
) : ProjectDirectoryScope {

  val runner: GradleRunner = GradleRunner.create().withProjectDir(projectDir.toFile())

  companion object {

    val projectTestTempDir: Path by systemProperty(Paths::get)

    /** Temporary directory for the functional tests. This directory will be auto-deleted. */
    val funcTestTempDir: Path by lazy {
      projectTestTempDir.resolve("functional-tests")
    }

    private val dokkaSourceDir: Path by systemProperty(Paths::get)
    /** Dokka Source directory that contains Gradle projects used for integration tests */
    val integrationTestProjectsDir: Path by systemProperty(Paths::get)
    /** Dokka Source directory that contains example Gradle projects */
    val exampleProjectsDir: Path by systemProperty(Paths::get)

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

fun GradleProjectTest.projectFile(
  @Language("TEXT")
  filePath: String
): PropertyDelegateProvider<Any?, ReadWriteProperty<Any?, String>> =
  PropertyDelegateProvider { _, _ ->
    TestProjectFileProvidedDelegate(this, filePath)
  }


/** Delegate for reading and writing a [GradleProjectTest] file. */
private class TestProjectFileProvidedDelegate(
  private val project: GradleProjectTest,
  private val filePath: String,
) : ReadWriteProperty<Any?, String> {
  override fun getValue(thisRef: Any?, property: KProperty<*>): String =
    project.projectDir.resolve(filePath).toFile().readText()

  override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
    project.createFile(filePath, value)
  }
}

/** Delegate for reading and writing a [GradleProjectTest] file. */
class TestProjectFileDelegate(
  private val filePath: String,
) : ReadWriteProperty<ProjectDirectoryScope, String> {
  override fun getValue(thisRef: ProjectDirectoryScope, property: KProperty<*>): String =
    thisRef.projectDir.resolve(filePath).toFile().readText()

  override fun setValue(thisRef: ProjectDirectoryScope, property: KProperty<*>, value: String) {
    thisRef.createFile(filePath, value)
  }
}


@DslMarker
annotation class ProjectDirectoryDsl

@ProjectDirectoryDsl
interface ProjectDirectoryScope {
  val projectDir: Path
}


fun ProjectDirectoryScope.createFile(filePath: String, contents: String): File =
  projectDir.resolve(filePath).toFile().apply {
    parentFile.mkdirs()
    createNewFile()
    writeText(contents)
  }


@ProjectDirectoryDsl
fun ProjectDirectoryScope.file(
  path: String
): Path = projectDir.resolve(path)


/** Set the content of `settings.gradle.kts` */
@delegate:Language("kts")
var ProjectDirectoryScope.settingsGradleKts: String by TestProjectFileDelegate("settings.gradle.kts")


/** Set the content of `build.gradle.kts` */
@delegate:Language("kts")
var ProjectDirectoryScope.buildGradleKts: String by TestProjectFileDelegate("build.gradle.kts")




fun GradleRunner.withEnvironment(vararg map: Pair<String, String>): GradleRunner =
  withEnvironment(map.toMap())
