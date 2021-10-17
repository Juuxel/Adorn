import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.KotlinterExtension

plugins {
    base
    kotlin("jvm") version "1.5.31" apply false

    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "0.10.0.180" apply false
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
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "architectury-plugin")
    apply(plugin = "io.github.juuxel.loom-quiltflower")
    apply(plugin = "org.jmailen.kotlinter")
    apply(plugin = "com.github.johnrengelman.shadow")

    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_16
        targetCompatibility = JavaVersion.VERSION_16
    }

    group = rootProject.group
    version = rootProject.version
    base.archivesBaseName = rootProject.base.archivesBaseName

    val bundle by configurations.creating {
        isCanBeConsumed = false
        isCanBeResolved = true
    }

    dependencies {
        "minecraft"("net.minecraft:minecraft:${rootProject.property("minecraft-version")}")
        "mappings"("net.fabricmc:yarn:${rootProject.property("minecraft-version")}+${rootProject.property("mappings")}:v2")
    }

    extensions.configure<KotlinterExtension> {
        disabledRules = arrayOf("parameter-list-wrapping")
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            options.release.set(16)
        }

        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "16"
        }

        "jar"(Jar::class) {
            from(rootProject.file("LICENSE"))
            archiveClassifier.set("dev-slim")
        }

        "shadowJar"(ShadowJar::class) {
            archiveClassifier.set("dev-shadow")
            configurations = listOf(bundle)
        }

        "remapJar"(RemapJarTask::class) {
            dependsOn("shadowJar")
            input.set(named<ShadowJar>("shadowJar").flatMap { it.archiveFile })
        }
    }

    // PLEASE REMOVE AFTEREVALUATE FROM LOOM
    afterEvaluate {
        tasks {
            "remapJar"(RemapJarTask::class) {
                archiveClassifier.set(project.name)
            }
        }
    }
}
