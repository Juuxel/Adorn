import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.60"
    application
}

base {
    archivesBaseName = "AdornJsonPlugin"
}

repositories {
    mavenCentral()
    maven(url = "http://server.bbkr.space:8081/artifactory/libs-release")
    maven(url = "http://server.bbkr.space:8081/artifactory/libs-snapshot")
}

version = "1.6.0"

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
