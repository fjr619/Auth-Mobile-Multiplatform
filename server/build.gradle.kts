plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinx.serialization)
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
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
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.jackson)
    implementation(libs.ktor.serialization.kotlinx)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.common.codec)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.postgresql)
    implementation(libs.hikari)
    implementation(libs.kotlinx.serialization)
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation ("io.insert-koin:koin-ktor:3.5.3")
    implementation ("io.insert-koin:koin-logger-slf4j:3.5.3")

    // Koin for Dependency Injection
    implementation("io.insert-koin:koin-annotations:1.3.0") // Koin Annotations for KSP
    ksp("io.insert-koin:koin-ksp-compiler:1.3.0") // Koin KSP Compiler for KSP

    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
}