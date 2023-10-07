plugins {
    id("adorn-data-generator")
}

architectury {
    // Set up Architectury for the common project.
    // This sets up the transformations (@ExpectPlatform etc.) we need for production environments.
    common(
        "fabric",
//        "forge",
    )
}

loom {
    accessWidenerPath.set(file("src/main/resources/adorn.accesswidener"))
}

dependencies {
    // Add dependencies on the required Kotlin modules.
    implementation(kotlin("stdlib-jdk8"))
    implementation("blue.endless:jankson:${rootProject.property("jankson")}")

    // Just for @Environment and mixin deps :)
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric-loader")}")

    // Add a mod dependency on some APIs for compat code.
    modCompileOnly("me.shedaniel:RoughlyEnoughItems-api:${rootProject.property("rei")}")
    modCompileOnly(libs.emi.common)
    modCompileOnly(libs.jei.fabric)
    compileOnly(libs.rei.annotations)
}

dataGenerator {
    generateTags.set(true)

    configs.register("all") {
        rootProject.subprojects {
            files.from(fileTree("src/data"))
        }
        tagsOnly.set(true)
    }
}
