import juuxel.adorn.datagen.gradle.DataGeneratorExtension
import juuxel.adorn.datagen.gradle.DeleteDuplicates
import juuxel.adorn.datagen.gradle.GenerateData
import juuxel.adorn.datagen.util.MinifyJson

plugins {
    java
}

val extension = extensions.create("dataGenerator", DataGeneratorExtension::class.java, project)

extension.configs.register("main") {
    files.from(fileTree("src/data").filter { it.extension == "xml" })
}

val generatedResources = layout.projectDirectory.dir("src/main/generatedResources")

val generateMainData by tasks.registering(GenerateData::class) {
    configs.set(extension.configs)
    generateTags.set(extension.generateTags)
    output.convention(generatedResources)
}

val generateData by tasks.registering {
    dependsOn(generateMainData)
}

tasks {
    processResources {
        mustRunAfter(generateData)
    }
}

val deleteDuplicateResources by tasks.registering(DeleteDuplicates::class) {
    generated.convention(generatedResources)
    main.convention(layout.dir(sourceSets.main.map { it.resources.srcDirs.first() }))
    mustRunAfter(generateMainData, generateData)
}

sourceSets {
    main {
        resources.srcDir(generatedResources)
    }
}

afterEvaluate {
    tasks.named("remapJar") {
        doLast(MinifyJson)
    }
}
