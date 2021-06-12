plugins {
    kotlin("jvm")
    id("architectury-plugin")
    id("dev.architectury.loom")
    id("org.jmailen.kotlinter")
}

architectury {
    common(false)
}

loom {
    accessWidener = file("src/main/resources/adorn.accesswidener")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    minecraft("net.minecraft:minecraft:${rootProject.property("minecraft-version")}")
    mappings("net.fabricmc:yarn:${rootProject.property("minecraft-version")}+${rootProject.property("mappings")}:v2")

    // Just for @Environment and mixin deps :)
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric-loader")}")
}

kotlinter {
    disabledRules = arrayOf("parameter-list-wrapping")
}

// PLEASE REMOVE AFTEREVALUATE FROM LOOM
afterEvaluate {
    tasks {
        remapJar {
            remapAccessWidener.set(false)
        }
    }
}
