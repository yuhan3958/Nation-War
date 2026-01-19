plugins {
    id("java")
}

subprojects {
    apply(plugin = "java")

    group = "com.nationWar"
    version = "1.0-SNAPSHOT"

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    repositories {
        mavenCentral()
        maven {
            name = "Forge"
            url = uri("https://maven.minecraftforge.net/")
        }
    }
}
