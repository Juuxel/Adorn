pluginManagement {
    repositories {
        jcenter()
        maven(url = "https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        gradlePluginPortal()
        maven {
            name = "JuuxelBintray"
            url = uri("https://dl.bintray.com/juuxel/maven")
        }
    }
}

rootProject.name = "adorn"
