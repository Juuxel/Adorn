plugins {
    id("adorn-data-generator")
}

val commonOutputs = configurations.create("commonOutputs") {
    isCanBeConsumed = true
    isCanBeResolved = false
}
val clientOutputs = configurations.create("clientOutputs") {
    isCanBeConsumed = true
    isCanBeResolved = false
}

loom {
    splitEnvironmentSourceSets()
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

    commonOutputs(sourceSets.getByName("main").output)
    clientOutputs(sourceSets.getByName("client").output)
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
