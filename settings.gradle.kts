rootProject.name = "nation-war"

include(
    ":core:core-common",
    ":core:core-client",
    ":core:core-server",
    ":core:core-mod",
    ":nation:nation-common",
    ":nation:nation-client",
    ":nation:nation-server",
    ":nation:nation-mod",
    ":tech:tech-common",
    ":tech:tech-client",
    ":tech:tech-server",
    ":tech:tech-mod",
    ":ore:ore-common",
    ":ore:ore-server",
    ":ore:ore-mod"
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.minecraftforge.net")
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://maven.minecraftforge.net")
        mavenCentral()
    }
}
