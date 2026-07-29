plugins {
    id("adorn-core")
}

tasks {
    // Register a custom "collect jars" task that copies the Fabric and Forge mod jars
    // into the root project's build/libs. This makes it easier for me to find them
    // for testing and releasing.
    val collectJars = register<Copy>("collectJars") {
        // Find the jar tasks of projects that aren't common (so :fabric and :forge) and depend on them.
        val tasks = subprojects.filter { it.path != ":common" }
            .map { it.tasks.named("jar") }
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
