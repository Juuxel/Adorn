import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    base
    kotlin("jvm") version "1.5.10" apply false

    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "0.10.0.174" apply false
    id("io.github.juuxel.loom-quiltflower") version "1.3.0" apply false

    id("org.jmailen.kotlinter") version "3.2.0" apply false
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
}

architectury {
    minecraft = project.property("minecraft-version").toString()
}

group = "io.github.juuxel"
version = "${project.property("mod-version")}+${project.property("minecraft-version")}"
base.archivesBaseName = "Adorn"

tasks {
    val collectJars by registering(Copy::class) {
        val tasks = subprojects.map { it.tasks.named("remapJar") }
        dependsOn(tasks)

        from(tasks)
        into(buildDir.resolve("libs"))
    }

    assemble {
        dependsOn(collectJars)
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_16
        targetCompatibility = JavaVersion.VERSION_16
    }

    group = rootProject.group
    version = rootProject.version
    base.archivesBaseName = rootProject.base.archivesBaseName

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.release.set(16)
        }

        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "15"
        }

        "jar"(Jar::class) {
            from(rootProject.file("LICENSE"))
            archiveClassifier.set("dev-slim")
        }
    }
}
