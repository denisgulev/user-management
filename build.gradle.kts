
val kotlin_version: String by project
val logback_version: String by project
val mongo_version: String by project
val ktor_version: String by project

// Koin
val koin_ktor_version: String by project
val koin_ksp_version: String by project

// BCrypt
val bcrypt_version: String by project

// Logger
val micrologging_version: String by project

plugins {
    kotlin("jvm") version "2.0.0"
    id("io.ktor.plugin") version "2.3.12"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    id("com.google.devtools.ksp") version "2.0.0-1.0.23"
}

group = "example.com"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-swagger-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm")
    implementation("io.ktor:ktor-server-auth-jwt-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    //MongoDB
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")

    // Koin for Dependency Injection
    implementation("io.insert-koin:koin-ktor:$koin_ktor_version") // Koin for Ktor
    implementation("io.insert-koin:koin-logger-slf4j:$koin_ktor_version") // Koin Logger
    implementation("io.insert-koin:koin-annotations:$koin_ksp_version") // Koin Annotations for KSP
    ksp("io.insert-koin:koin-ksp-compiler:$koin_ksp_version")

    // BCrypt
    implementation("com.ToxicBakery.library.bcrypt:bcrypt:$bcrypt_version")

    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:$micrologging_version")
}

ksp {
    arg("koin.generated", "true")
}
