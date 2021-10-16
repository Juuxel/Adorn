import dev.architectury.plugin.TransformingTask

plugins {
    kotlin("jvm")
    id("architectury-plugin")
    id("dev.architectury.loom")
    id("io.github.juuxel.loom-quiltflower")
    id("org.jmailen.kotlinter")
    id("com.github.johnrengelman.shadow")
}

val bundle by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

val dev by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

configurations {
    compileClasspath {
        extendsFrom(bundle)
    }

    runtimeClasspath {
        extendsFrom(bundle)
    }
}

architectury {
    common()
}

loom {
    accessWidenerPath.set(file("src/main/resources/adorn.accesswidener"))
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    minecraft("net.minecraft:minecraft:${rootProject.property("minecraft-version")}")
    mappings("net.fabricmc:yarn:${rootProject.property("minecraft-version")}+${rootProject.property("mappings")}:v2")
    bundle("blue.endless:jankson:${rootProject.property("jankson")}")

    // Just for @Environment and mixin deps :)
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric-loader")}")
}

kotlinter {
    disabledRules = arrayOf("parameter-list-wrapping")
}

tasks {
    remapJar {
        input.set(shadowJar.flatMap { it.archiveFile })
        archiveClassifier.set("common")
    }

    shadowJar {
        archiveClassifier.set("dev-shadow")
        configurations = listOf(bundle)
        relocate("blue.endless.jankson", "juuxel.adorn.relocated.jankson")
    }

    for (platform in arrayOf("Fabric", "Forge")) {
        "transformProduction$platform"(TransformingTask::class) {
            input.set(shadowJar.flatMap { it.archiveFile })
        }
    }
}

// PLEASE REMOVE AFTEREVALUATE FROM LOOM
afterEvaluate {
    tasks {
        remapJar {
            remapAccessWidener.set(false)
        }
    }
}

artifacts.add("dev", tasks.shadowJar)
