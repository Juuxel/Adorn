import juuxel.adorn.datagen.gradle.DeleteDuplicates
import juuxel.adorn.datagen.gradle.GenerateData
import juuxel.adorn.datagen.gradle.GenerateTags

plugins {
    java
}

val generatedResources = layout.projectDirectory.dir("src/main/generatedResources")

val generateMainData by tasks.registering(GenerateData::class) {
    configs.from(fileTree("src/data").filter { it.extension == "xml" })
    output.convention(generatedResources)
}

val generateTags by tasks.registering(GenerateTags::class) {
    dependsOn(generateMainData)
    output.convention(generatedResources)
}

val generateData by tasks.registering {
    dependsOn(generateMainData, generateTags)
}

val deleteDuplicateResources by tasks.registering(DeleteDuplicates::class) {
    generated.convention(generatedResources)
    main.convention(layout.dir(sourceSets.main.map { it.resources.srcDirs.first() }))
    mustRunAfter(generateMainData, generateTags, generateData)
}

sourceSets {
    main {
        resources.srcDir(generatedResources)
    }
}
