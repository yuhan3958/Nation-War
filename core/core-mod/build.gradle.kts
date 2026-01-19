plugins {
    `java-library` // Explicitly apply java-library plugin
    id("net.minecraftforge.gradle") version "6.0.14" // Use a fixed version
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-client"))
    implementation(project(":core:core-server"))
    minecraft("net.minecraftforge:forge:1.20.1-47.1.0")

    // Allow CoreMod to see event classes from other modules without creating a hard dependency
    compileOnly(project(":nation:nation-common"))
    compileOnly(project(":nation:nation-mod"))
}

minecraft {
    mappings("official", "1.20.1")

    runs {
        create("client") {
            workingDirectory(project.file("run"))
            property("forge.logging.console.level", "debug")
            mods {
                create("coremod") {
                    source(sourceSets.main.get())
                }
                create("nationmod") {
                    source(project(":nation:nation-mod").sourceSets.main.get())
                }
                create("techmod") {
                    source(project(":tech:tech-mod").sourceSets.main.get())
                }
                create("oremod") {
                    source(project(":ore:ore-mod").sourceSets.main.get())
                }
            }
        }

        create("server") {
            workingDirectory(project.file("run"))
            property("forge.logging.console.level", "debug")
            mods {
                create("coremod") {
                    source(sourceSets.main.get())
                }
                create("nationmod") {
                    source(project(":nation:nation-mod").sourceSets.main.get())
                }
                create("techmod") {
                    source(project(":tech:tech-mod").sourceSets.main.get())
                }
                create("oremod") {
                    source(project(":ore:ore-mod").sourceSets.main.get())
                }
            }
        }
    }
}
