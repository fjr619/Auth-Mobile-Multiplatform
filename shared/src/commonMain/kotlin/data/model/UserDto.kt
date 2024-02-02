package data.model

import io.ktor.http.ContentType
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class ServiceResultSerializer<T : Any>(
    tSerializer: KSerializer<T>
) : KSerializer<Response<T>> {

//    val logger = KoinJavaComponent.inject<KLogger>(KLogger::class.java).value

    @Serializable
    @SerialName("Response")
    data class ServiceResultSurrogate<T : Any> @OptIn(ExperimentalSerializationApi::class) constructor(
        val type: Type,
        // The annotation is not necessary, but it avoids serializing "data = null"
        // for "Error" results.
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        val data: T? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        val message: String? = null,

        val statusCode: Int = -1,
        val token: String? = null
    ) {
        enum class Type { SUCCESS, ERROR }
    }

    private val surrogateSerializer = ServiceResultSurrogate.serializer(tSerializer)

    override val descriptor: SerialDescriptor = surrogateSerializer.descriptor

    override fun deserialize(decoder: Decoder): Response<T> {
        val surrogate = surrogateSerializer.deserialize(decoder)
        return when (surrogate.type) {
            ServiceResultSurrogate.Type.SUCCESS ->
                if (surrogate.data != null)
                    Response(
                        data = surrogate.data,
                        type = surrogate.type,
                        message = surrogate.message,
                        token = surrogate.token,
                        statusCode = surrogate.statusCode
                        )
                else
                    throw SerializationException("Missing data for successful result")

            ServiceResultSurrogate.Type.ERROR -> {

                Response(
                    data = surrogate.data,
                    type = surrogate.type,
                    message = surrogate.message,
                    token = surrogate.token,
                    statusCode = surrogate.statusCode)
            }


        }
    }

    override fun serialize(encoder: Encoder, value: Response<T>) {
        val surrogate = ServiceResultSurrogate(
            data = value.data,
            message = value.message,
            statusCode = value.statusCode,
            token = value.token,
            type = value.type
        )

        surrogateSerializer.serialize(encoder, surrogate)
    }
}

@Serializable(with = ServiceResultSerializer::class)
data class Response <out T: Any> (
    val data: T? = null,
    val message: String? = null,
    val statusCode: Int = -1,
    val token: String? = null,
    val type: ServiceResultSerializer.ServiceResultSurrogate.Type = ServiceResultSerializer.ServiceResultSurrogate.Type.SUCCESS
)
@Serializable
data class UserDto(
    val id: Long,
    val fullName: String,
    val avatar: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
//    var authToken: String? = null,
//    val role: User.Role = User.Role.USER
)

@Serializable
data class UserLoginDto(
    val email: String,
    val password: String
)