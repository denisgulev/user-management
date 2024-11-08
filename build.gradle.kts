val kotlinVersion: String by project
val logbackVersion: String by project
val mongoVersion: String by project
val ktorVersion: String by project

// Koin
val koinVersion: String by project
val kspVersion: String by project

// BCrypt
val bcryptVersion: String by project

// Logger
val microloggingVersion: String by project

plugins {
    kotlin("jvm") version "2.0.20"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.20"
    id("io.ktor.plugin") version "2.3.12"
    id("com.google.devtools.ksp") version "2.0.20-1.0.24"
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
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-swagger-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-config-yaml:$ktorVersion")
//    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")

    //MongoDB
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")

    // Koin for Dependency Injection
    implementation("io.insert-koin:koin-ktor:$koinVersion") // Koin for Ktor
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion") // Koin Logger
    implementation("io.insert-koin:koin-annotations:$kspVersion") // Koin Annotations for KSP
    ksp("io.insert-koin:koin-ksp-compiler:$kspVersion")

    // BCrypt
    implementation("com.ToxicBakery.library.bcrypt:bcrypt:$bcryptVersion")

    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:$microloggingVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    // Kotlin Serialization JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    // Testing
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    implementation(kotlin("stdlib-jdk8"))
}

ksp {
    arg("koin.generated", "true")
}