import io.ktor.http.HttpStatusCode
import kotlinx.serialization.Serializable

@Serializable(with = ServiceResultSerializer::class)
sealed class Response<out T: Any>(
    open val data: T?,
    open val message: String?,
    open val type: Type,
    open val token: String?,
    open val statusCode: Int,
) {
    enum class Type { SUCCESS, ERROR }

    data class SuccessResponse<out T : Any>(
        override val data: T?,
        override val message: String? = null,
        override val type: Type = Type.SUCCESS,
        override val token: String? = null,
        override val statusCode: Int = HttpStatusCode.OK.value
    ) : Response<T>(data, message, type, token, statusCode) {
        fun toResponse() = this as Response<T>
    }


    data class ErrorResponse(
        override val message: String? = null,
        override val type: Type = Type.ERROR,
        override val token: String? = null,
        override val statusCode: Int = HttpStatusCode.BadRequest.value
    ) : Response<Nothing>(null, message, type, token, statusCode) {
        fun toResponse() = this as Response<Nothing>
    }
}