plugins {
    id("adorn-data-generator")
}

architectury {
    // Set up Architectury for the common project.
    // This sets up the transformations (@ExpectPlatform etc.) we need for production environments.
    common(
        "fabric",
        "forge",
    )
}

loom {
    accessWidenerPath.set(file("src/main/resources/adorn.accesswidener"))
}

dependencies {
    // Add dependencies on the required Kotlin modules.
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("blue.endless:jankson:${rootProject.property("jankson")}")

    // Just for @Environment and mixin deps :)
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric-loader")}")

    // Add a mod dependency on REI's API for compat code.
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api:${rootProject.property("rei")}")
}

tasks {
    generateTags {
        // Include all src/data files from all subprojects
        // for tag generation.
        rootProject.subprojects {
            configs.from(fileTree("src/data"))
        }
    }
}
