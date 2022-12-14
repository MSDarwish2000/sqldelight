import app.cash.sqldelight.gradle.SqlDelightExtension

buildscript {
  apply(from = "${projectDir.absolutePath}/../buildscript.gradle")
}

apply(plugin = "org.jetbrains.kotlin.jvm")
apply(plugin = "app.cash.sqldelight")

repositories {
  maven("file://${projectDir.absolutePath}/../../../../build/localMaven")
  maven { url "https://s01.oss.sonatype.org/content/repositories/snapshots" }
  mavenCentral()
}

configure<SqlDelightExtension> {
  databases {
    create("Database") {
      packageName.set("com.example")
      schemaOutputDirectory.set(file("src/main/sqldelight/databases"))
    }
  }
}

tasks.register("checkSources") {
  val kotlinSourceDirectorySet = (project.extensions["kotlin"] as org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension)
    .sourceSets["main"]
    .kotlin

  val output = file("build/sourcesChecked")
  inputs.files(kotlinSourceDirectorySet)
  outputs.file(output)
  doLast {
    check(kotlinSourceDirectorySet.files.any { it.name == "Database.kt" }) {
      "Database.kt was not generated"
    }
    output.writeText("Sources were generated as a dependency of checkSources ✅")
  }
}
