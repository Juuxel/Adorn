import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.4.30" apply false

    id("architectury-plugin") version "3.2-SNAPSHOT"
    id("dev.architectury.loom") version "0.8.0-SNAPSHOT" apply false

    id("org.jmailen.kotlinter") version "3.2.0" apply false
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
}

architectury {
    minecraft = project.property("minecraft-version").toString()
}

group = "io.github.juuxel"
version = "${project.property("mod-version")}+${project.property("minecraft-version")}"
base.archivesBaseName = "Adorn"

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    group = rootProject.group
    version = rootProject.version
    base.archivesBaseName = rootProject.base.archivesBaseName

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.release.set(8)
        }

        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
}
