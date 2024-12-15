plugins {
    kotlin("jvm") version "2.0.21"
}

group = "com.omarsahl"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("org.openjdk.jol:jol-core:0.17")
}

tasks.test {
    useJUnitPlatform()
}