package com.fjr619.jwtpostgresql.base

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}


class ServiceResultSerializer<T : Any>(
    tSerializer: KSerializer<T>
) : KSerializer<BaseResponse<T>> {
    @Serializable
    @SerialName("BaseResponse")
    data class ServiceResultSurrogate<T : Any>(
        val type: Type,
        // The annotation is not necessary, but it avoids serializing "data = null"
        // for "Error" results.
//        @EncodeDefault(EncodeDefault.Mode.NEVER)
        val data: T? = null,
//        @EncodeDefault(EncodeDefault.Mode.NEVER)
        val message: String? = null,

        val statusCode: Int = -1
    ) {
        enum class Type { SUCCESS, ERROR }
    }

    private val surrogateSerializer = ServiceResultSurrogate.serializer(tSerializer)

    override val descriptor: SerialDescriptor = surrogateSerializer.descriptor

    override fun deserialize(decoder: Decoder): BaseResponse<T> {
        val surrogate = surrogateSerializer.deserialize(decoder)
        return when (surrogate.type) {
            ServiceResultSurrogate.Type.SUCCESS ->
                if (surrogate.data != null)
                    BaseResponse.SuccessResponse(surrogate.data)
                else
                    throw SerializationException("Missing data for successful result")

            ServiceResultSurrogate.Type.ERROR ->
                BaseResponse.ErrorResponse(message = surrogate.message)
        }
    }

    override fun serialize(encoder: Encoder, value: BaseResponse<T>) {
        val surrogate = when (value) {
            is BaseResponse.ErrorResponse -> {
                ServiceResultSurrogate(
                    ServiceResultSurrogate.Type.ERROR,
                    message = value.message,
                    statusCode = value.statusCode.value
                )
            }

            is BaseResponse.SuccessResponse -> {
                ServiceResultSurrogate(
                    ServiceResultSurrogate.Type.SUCCESS,
                    data = value.data,
                    statusCode = value.statusCode.value
                )
            }
        }
        surrogateSerializer.serialize(encoder, surrogate)
    }
}