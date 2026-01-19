plugins {
    `kotlin-dsl`
    id("net.minecraftforge.gradle") version "6.0.+"
}

dependencies {
    implementation(project(":ore:ore-common"))
    implementation(project(":ore:ore-server"))
    minecraft("net.minecraftforge:forge:1.20.1-47.1.0")
}

minecraft {
    mappings("official", "1.20.1")
}
