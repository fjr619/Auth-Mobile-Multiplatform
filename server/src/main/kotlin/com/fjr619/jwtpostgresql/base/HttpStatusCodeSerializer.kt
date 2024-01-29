package com.fjr619.jwtpostgresql.base

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import mu.KotlinLogging

//https://dev.to/simonnystrom/adding-contextual-serialization-in-ktor-1pp8
object HttpStatusCodeSerializer : KSerializer<HttpStatusCode> {
    override fun deserialize(decoder: Decoder): HttpStatusCode =
        HttpStatusCode.fromValue(decoder.decodeInt())


    override fun serialize(encoder: Encoder, value: HttpStatusCode) {
        encoder.encodeInt(value.value)
    }

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("HttpStatusCode", PrimitiveKind.INT)
}