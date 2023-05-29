Reproducer for https://github.com/gradle/gradle/issues/23959

#### GitHub action test

See the GitHub workflow for the full log

https://github.com/aSemy/bug-gradle-testkit/actions/workflows/tests.yml

#### Manual test

1. Load on Windows
2. Run 
   ```shell
   ./gradlew check
   ```
3. The test fails
   ```
   FAILURE: Build failed with an exception.
   
   * Where:
     Build file '~\testkit-windows\modules\integration-tests\build\test-temp-dir\it\it-basic\dokka\build.gradle.kts' line: 8
   
   * What went wrong:
     Plugin [id: 'org.jetbrains.kotlin.jvm', version: '1.7.22'] was not found in any of the following sources:
   ```

But, when opened directly, the project works

1. Open the project
   ```shell
   cd modules/integration-tests/build/test-temp-dir/it/it-basic/dokka
   ```
2. in `modules/integration-tests/projects/it-basic/build.gradle.kts` replace `${System.getenv("DOKKA_VERSION")}` with `1.7.20`
3. Run
   ```shell
   ./gradlew check
   ```
4. it works
