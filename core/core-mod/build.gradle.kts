plugins {
    `kotlin-dsl`
    id("net.minecraftforge.gradle") version "6.0.+"
}

dependencies {
    implementation(project(":core:core-common"))
    implementation(project(":core:core-client"))
    implementation(project(":core:core-server"))
    minecraft("net.minecraftforge:forge:1.20.1-47.1.0")
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
