plugins {
    kotlin("jvm") version "2.2.20"
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.6"
}

repositories {
    mavenCentral()
}

subprojects {
    repositories {
        mavenCentral()
    }
}
