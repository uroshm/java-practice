# Java Practice

A collection of Java exercises exploring data structures, algorithms,
object-oriented design, and testing, with room for system design notes.

## Current exercise

An in-memory filesystem with files and directories, recursive depth-first search
by ID, node insertion, and node removal. The existing test loads the Spring Boot
context and exercises a lookup; broader behavioral coverage is still a practice
task.

## Getting started

Install JDK 21. The included Gradle wrapper downloads Gradle and dependencies on
the first build; no separate Gradle installation is needed.

```sh
./gradlew test
./gradlew build
```

On Windows, use `gradlew.bat` instead of `./gradlew`.

Run only the filesystem tests:

```sh
./gradlew :app:test --tests 'practice.java.filesystem.FileSystemExampleTest'
```

Test reports are written to `app/build/reports/tests/test/index.html`.

## Repository layout

```text
app/
  build.gradle                         Java 21, Spring Boot, Lombok, and JUnit
  src/main/java/practice/java/
    App.java                           Spring Boot entry point
    filesystem/FileSystemExample.java  Filesystem design exercise
  src/main/resources/
    application.properties             Application configuration
  src/test/java/practice/java/
    filesystem/FileSystemExampleTest.java
.github/workflows/build.yml            Build and test on pushes and pull requests
gradle/wrapper/                        Gradle wrapper files
```

## Practice workflow

For each exercise:

1. Write down the requirements, assumptions, and edge cases.
2. Explain the approach and time and space complexity.
3. Implement a solution and test normal cases, boundaries, and invalid inputs.
4. Record tradeoffs, follow-up questions, and improvements after review.

Areas to expand include data structures and algorithms, object-oriented design,
and system design discussions covering scale, reliability, and tradeoffs.

## Spring Boot app

The project also includes a Spring Boot application shell:

```sh
./gradlew :app:bootRun
```

The server uses port 8080 by default. No HTTP endpoints are implemented yet.
To use another port:

```sh
./gradlew :app:bootRun --args='--server.port=8081'
```

After building, run the packaged application with:

```sh
java -jar app/build/libs/app-0.0.1-SNAPSHOT.jar
```
