import moe.nikky.counter.CounterExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
    id("moe.nikky.persistentCounter") version "0.0.8-SNAPSHOT"
}

base {
    archivesBaseName = "AdornJsonPlugin"
}

repositories {
    mavenCentral()
    maven(url = "http://server.bbkr.space:8081/artifactory/libs-release")
    maven(url = "http://server.bbkr.space:8081/artifactory/libs-snapshot")
}

val counter: CounterExtension = project.extensions.getByType()
val buildNumber = counter.variable("buildNumber", rootProject.version.toString())

version = "${rootProject.version}-$buildNumber"

dependencies {
    val jsonFactory = "0.5.0-beta.3-SNAPSHOT"
    implementation("io.github.cottonmc:json-factory:$jsonFactory")
    implementation("io.github.cottonmc:json-factory-gui:$jsonFactory") { exclude(module = "example-plugin") }
}

application {
    mainClassName = "juuxel.adorn.json.AdornPlugin"
}

tasks.getByName<JavaExec>("run") {
    workingDir = project.mkdir("run")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xuse-experimental=kotlin.Experimental"
        jvmTarget = "1.8"
    }
}
