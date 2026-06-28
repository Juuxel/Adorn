plugins {
    `java-gradle-plugin`
}

repositories {
    mavenCentral()

    maven("https://maven.fabricmc.net")
    maven("https://maven.architectury.dev")
    maven("https://maven.minecraftforge.net")
}

dependencies {
    implementation("com.google.code.gson:gson:2.14.0")
    implementation("org.ow2.asm:asm-tree:9.4")
    implementation("net.fabricmc:mapping-io:0.8.0")
    implementation("io.github.juuxel:adorn-data-generator")

    // Must match the version in settings.gradle.kts!
    implementation("dev.architectury:architectury-loom:1.17.+")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

gradlePlugin {
    plugins {
        register("adorn-core") {
            id = "adorn-core"
            implementationClass = "juuxel.adorn.gradle.CorePlugin"
        }

        register("adorn-minecraft-setup") {
            id = "adorn-minecraft-setup"
            implementationClass = "juuxel.adorn.gradle.MinecraftSetupPlugin"
        }

        register("adorn-platform-module") {
            id = "adorn-platform-module"
            implementationClass = "juuxel.adorn.gradle.PlatformModulePlugin"
        }

        register("adorn-data-generator") {
            id = "adorn-data-generator"
            implementationClass = "juuxel.adorn.gradle.DataGeneratorPlugin"
        }

        register("adorn-data-generator.modular") {
            id = "adorn-data-generator.modular"
            implementationClass = "juuxel.adorn.gradle.ModularDataGeneratorPlugin"
        }
    }
}
