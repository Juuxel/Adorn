plugins {
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.ow2.asm:asm-tree:9.4")
    implementation("io.github.juuxel:adorn-data-generator")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

gradlePlugin {
    plugins {
        register("adorn-data-generator") {
            id = "adorn-data-generator"
            implementationClass = "juuxel.adorn.gradle.DataGeneratorPlugin"
        }

        register("adorn-data-generator.modular") {
            id = "adorn-data-generator.modular"
            implementationClass = "juuxel.adorn.gradle.ModularDataGeneratorPlugin"
        }

        register("adorn-minify-json") {
            id = "adorn-minify-json"
            implementationClass = "juuxel.adorn.gradle.MinifyJsonPlugin"
        }

        register("adorn-service-inline") {
            id = "adorn-service-inline"
            implementationClass = "juuxel.adorn.gradle.ServiceInlinePlugin"
        }
    }
}
