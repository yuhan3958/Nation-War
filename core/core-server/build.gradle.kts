plugins {
    `java-library`
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.dynmap.us/repository/dynmap/") }
}

dependencies {
    api(project(":core:core-common"))
    compileOnly("us.dynmap:dynmap-api:3.7-beta-1")
    compileOnly("us.dynmap:Dynmap-Forge:3.7-beta-1")
}
