import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.KotlinterExtension

plugins {
    base
    kotlin("jvm") version "1.6.0" apply false

    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "0.11.0.248" apply false
    id("io.github.juuxel.loom-quiltflower") version "1.6.0" apply false

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
        val tasks = subprojects.filter { it.path != ":common" }.map { it.tasks.named("remapJar") }
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

    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    group = rootProject.group
    version = rootProject.version
    base.archivesName.set(rootProject.base.archivesName)

    repositories {
        exclusiveContent {
            forRepository {
                ivy {
                    url = uri("https://github.com/Juuxel/Menu/archive/refs/tags")
                    patternLayout {
                        artifact("[revision].zip")
                    }
                    metadataSources {
                        artifact()
                    }
                }
            }

            filter {
                includeModule("io.github.juuxel", "menu")
            }
        }
    }

    dependencies {
        "minecraft"("net.minecraft:minecraft:${rootProject.property("minecraft-version")}")
        val loom = project.extensions.getByName<net.fabricmc.loom.api.LoomGradleExtensionAPI>("loom")
        "mappings"(loom.layered {
            mappings("net.fabricmc:yarn:${rootProject.property("minecraft-version")}+${rootProject.property("yarn-mappings")}:v2")
            val menuVersion = rootProject.property("menu-mappings").toString()
            mappings("io.github.juuxel:menu:$menuVersion") {
                enigmaMappings()
                mappingPath("Menu-${menuVersion.replace('+', '-')}/mappings")
            }
        })
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
                inputFile.set(named<ShadowJar>("shadowJar").flatMap { it.archiveFile })
                archiveClassifier.set(project.name)
            }
        }
    }
}
