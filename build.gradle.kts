import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.KotlinterExtension

plugins {
    base
    kotlin("jvm") version "1.6.0" apply false

    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "0.10.0.210" apply false
    id("io.github.juuxel.loom-quiltflower-mini") version "1.2.1" apply false

    id("org.jmailen.kotlinter") version "3.2.0" apply false
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
}

architectury {
    minecraft = project.property("minecraft-version").toString()
}

group = "io.github.juuxel"
version = "${project.property("mod-version")}+${project.property("minecraft-version")}"
base.archivesName.set("Adorn")

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
    apply(plugin = "io.github.juuxel.loom-quiltflower-mini")
    apply(plugin = "org.jmailen.kotlinter")

    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    group = rootProject.group
    version = rootProject.version
    base.archivesName.set(rootProject.base.archivesName)

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
            options.release.set(17)
        }

        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "17"
        }

        "jar"(Jar::class) {
            from(rootProject.file("LICENSE"))
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

subprojects {
    if (path != ":common") {
        apply(plugin = "com.github.johnrengelman.shadow")

        val bundle by configurations.creating {
            isCanBeConsumed = false
            isCanBeResolved = true
        }

        tasks {
            "jar"(Jar::class) {
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
    }
}
