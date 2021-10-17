import dev.architectury.plugin.TransformingTask

val dev by configurations.creating {
    isCanBeResolved = false
    isCanBeConsumed = true
}

configurations {
    compileClasspath {
        extendsFrom(bundle.get())
    }

    runtimeClasspath {
        extendsFrom(bundle.get())
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
    bundle("blue.endless:jankson:${rootProject.property("jankson")}")

    // Just for @Environment and mixin deps :)
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric-loader")}")
}

tasks {
    shadowJar {
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
