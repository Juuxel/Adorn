includeBuild("../datagen") {
    dependencySubstitution {
        substitute(module("io.github.juuxel:adorn-data-generator")).using(project(":"))
    }
}
