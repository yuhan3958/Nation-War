// Configurations for ALL projects (root + subprojects)
allprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://maven.minecraftforge.net/") }
        maven { url = uri("https://repo.dynmap.us/repository/dynmap/") }
    }
}

// Configurations for ONLY subprojects that have source code
subprojects {
    if (file("src").exists()) {
        apply(plugin = "java")

        group = "com.nationWar"
        version = "1.0-SNAPSHOT"

        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }
}
