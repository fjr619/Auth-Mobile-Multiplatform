import io.github.oshai.kotlinlogging.KLogger
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

//https://stackoverflow.com/a/71860558
class ServiceResultSerializer<T : Any>(
    tSerializer: KSerializer<T>
) : KSerializer<Response<T>>, KoinComponent {

//    val logger = KoinJavaComponent.inject<KLogger>(KLogger::class.java).value
    val logger: KLogger by inject()

    @Serializable
    @SerialName("BaseResponse")
    data class ServiceResultSurrogate<T : Any> @OptIn(ExperimentalSerializationApi::class) constructor(
        val type: Response.Type,
        // The annotation is not necessary, but it avoids serializing "data = null"
        // for "Error" results.
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        val data: T? = null,
        @EncodeDefault(EncodeDefault.Mode.NEVER)
        val message: String? = null,

        val statusCode: Int = -1,
        val token: String? = null
    ) {

    }

    private val surrogateSerializer = ServiceResultSurrogate.serializer(tSerializer)

    override val descriptor: SerialDescriptor = surrogateSerializer.descriptor

    override fun deserialize(decoder: Decoder): Response<T> {
        logger.info { "deserialize" }
        val surrogate = surrogateSerializer.deserialize(decoder)
        return when (surrogate.type) {
            Response.Type.SUCCESS ->
                if (surrogate.data != null)
                    Response.SuccessResponse(
                        data = surrogate.data,
                        type = surrogate.type,
                        message = surrogate.message,
                        token = surrogate.token,
                        statusCode = surrogate.statusCode
                    )
                else
                    throw SerializationException("Missing data for successful result")

            Response.Type.ERROR ->
                Response.ErrorResponse(
                    type = surrogate.type,
                    message = surrogate.message,
                    token = surrogate.token,
                    statusCode = surrogate.statusCode
                )
        }
    }

    override fun serialize(encoder: Encoder, value: Response<T>) {
        logger.info { "serialize" }
        val surrogate = when (value) {
            is Response.ErrorResponse -> {
                ServiceResultSurrogate(
                    type = Response.Type.ERROR,
                    message = value.message,
                    statusCode = value.statusCode,
                    token = value.token
                )
            }

            is Response.SuccessResponse -> {
                ServiceResultSurrogate(
                    type = Response.Type.SUCCESS,
                    data = value.data,
                    statusCode = value.statusCode,
                    token = value.token
                )
            }
        }
        surrogateSerializer.serialize(encoder, surrogate)
    }
}