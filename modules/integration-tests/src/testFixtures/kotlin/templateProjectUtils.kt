package dev.adamko.dokkatoo.utils




fun GradleProjectTest.copyIntegrationTestProject(path: String) {
  GradleProjectTest.integrationTestProjectsDir
    .resolve(path)
    .toFile()
    .copyRecursively(projectDir.toFile(), overwrite = true) { _, _ -> OnErrorAction.SKIP }
}
