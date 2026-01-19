plugins {
    `kotlin-dsl`
    id("net.minecraftforge.gradle") version "6.0.+"
}

dependencies {
    implementation(project(":tech:tech-common"))
    implementation(project(":tech:tech-client"))
    implementation(project(":tech:tech-server"))
    minecraft("net.minecraftforge:forge:1.20.1-47.1.0")
}

minecraft {
    mappings("official", "1.20.1")
}
