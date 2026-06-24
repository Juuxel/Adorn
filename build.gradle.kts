plugins {
    // Apply the base plugin which mostly defines useful "build lifecycle" tasks like
    // assemble, check and build. The root project doesn't contain any code,
    // so we won't apply those plugins here. Only the assemble task is used in the root project.
    // See https://docs.gradle.org/current/userguide/base_plugin.html.
    base
}

// Set up basic Maven artifact metadata, including the project version
// and archive names.
group = "io.github.juuxel"
// Set the project version to be <mod version>+<Minecraft version> so the MC version is semver build metadata.
// The "mod-version" and "minecraft-version" properties are read from gradle.properties.
version = "${project.property("mod-version")}+${project.property("minecraft-version")}"
base.archivesName.set("Adorn")

tasks {
    // Register a custom "collect jars" task that copies the Fabric and Forge mod jars
    // into the root project's build/libs. This makes it easier for me to find them
    // for testing and releasing.
    val collectJars = register<Copy>("collectJars") {
        // Find the remapJar tasks of projects that aren't common (so :fabric and :forge) and depend on them.
        val tasks = subprojects.filter { it.path != ":common" }
            .map { it.tasks.named("remapJar") }
        dependsOn(tasks)

        // Copy the outputs of the tasks...
        from(tasks)
        // ...into build/libs.
        into(layout.buildDirectory.dir("libs"))
    }

    // Set up assemble to depend on the collectJars task, so it gets run on gradlew build.
    assemble {
        dependsOn(collectJars)
    }

    // This is for IDEA. If "classes" doesn't exist, it runs "assemble" - which
    // builds the final project jars and is slow - when you press the hammer icon.
    register("classes")
}
