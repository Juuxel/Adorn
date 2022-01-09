plugins {
    id("adorn-datagen")
}

architectury {
    common()
}

loom {
    accessWidenerPath.set(file("src/main/resources/adorn.accesswidener"))
}

datagen {
    wood("minecraft:oak")
    wood("minecraft:spruce")
    wood("minecraft:birch")
    wood("minecraft:jungle")
    wood("minecraft:acacia")
    wood("minecraft:crimson", fungus = true)
    wood("minecraft:warped", fungus = true)
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("blue.endless:jankson:${rootProject.property("jankson")}")

    // Just for @Environment and mixin deps :)
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric-loader")}")
}

// PLEASE REMOVE AFTEREVALUATE FROM LOOM
afterEvaluate {
    tasks {
        remapJar {
            remapAccessWidener.set(false)
        }
    }
}
