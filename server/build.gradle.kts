plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp) // KSP for Koin Annotations
    alias(libs.plugins.dokka) // Dokka for documentation
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
    implementation(libs.ktor.server.request.validation)

    // Server Status Pages
    implementation(libs.ktor.server.status.pages)

    // Auth JWT
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)

    //Koin
    implementation (libs.koin.ktor)
    implementation (libs.koin.logger.slf4j)
    implementation(libs.koin.annotations) // Koin Annotations for KSP
    ksp(libs.koin.ksp.compiler) // Koin KSP Compiler for KSP

    // Database
    implementation(libs.common.codec)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.postgresql)
    implementation(libs.hikari)

    // CORS
    implementation(libs.ktor.server.cors)

    // To generate Swagger UI
    implementation(libs.ktor.swagger.ui)

    // Logging
    implementation(libs.logback)
    implementation(libs.kotlin.logging.jvm)

    implementation(libs.kotlinx.datetime)

    implementation(libs.kotlin.result)

    // *** Testing *** //
    // Ktor Test
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.ktor.server.test.host)
    implementation(libs.ktor.client.content.negotiation) // For testing with Ktor Client JSON
    implementation(libs.ktor.client.auth) // For testing with Ktor Client Auth JWT

    // Kotlin Test
    testImplementation(libs.kotlin.test.junit)

    // JUnit 5 instead of JUnit 4
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.engine)

    // To test coroutines and suspend functions
    testImplementation(libs.kotlinx.coroutines.test)

    // MockK to test with mocks
    testImplementation(libs.mockk)
}