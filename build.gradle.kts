plugins {
    kotlin("jvm") version "2.0.21"
}

group = "com.omarsahl"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}