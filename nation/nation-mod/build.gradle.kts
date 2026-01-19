
plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(project(":nation:nation-common"))
    implementation(project(":nation:nation-client"))
    implementation(project(":nation:nation-server"))
}
