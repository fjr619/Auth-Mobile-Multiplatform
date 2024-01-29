plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinx.serialization)

    // KSP for Koin Annotations
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"

    // Dokka for documentation
    id("org.jetbrains.dokka") version "1.9.10"
    application
}

group = "com.fjr619.jwtpostgresql"
version = "1.0.0"
application {
    mainClass.set("com.fjr619.jwtpostgresql.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["development"] ?: "false"}")
}

dependencies {
    implementation(projects.shared)

    // Ktor Core
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)

    // Content Negotiation and Serialization
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.serialization.kotlinx)
    implementation(libs.kotlinx.serialization)

    // Content Validation
    implementation("io.ktor:ktor-server-request-validation:2.3.7")

    // Server Status Pages
    implementation(libs.ktor.server.status.pages)

    // Auth JWT
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)

    //Koin
    implementation ("io.insert-koin:koin-ktor:3.5.3")
    implementation ("io.insert-koin:koin-logger-slf4j:3.5.3")
    implementation("io.insert-koin:koin-annotations:1.3.0") // Koin Annotations for KSP
    ksp("io.insert-koin:koin-ksp-compiler:1.3.0") // Koin KSP Compiler for KSP

    // Database
    implementation(libs.common.codec)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.postgresql)
    implementation(libs.hikari)

    // CORS
    implementation("io.ktor:ktor-server-cors:2.3.7")

    // To generate Swagger UI
    implementation("io.github.smiley4:ktor-swagger-ui:2.2.0")

    // Logging
    implementation(libs.logback)
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")


    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
}