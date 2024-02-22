package domain.extension

import GENERIC_ERROR
import INVALID_AUTHENTICATION_TOKEN
import Response
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T: Any> Flow<Response<T>>.handleResult(
    scope: CoroutineScope,
    onSuccess:(T, String) -> Unit,
    onError:(message: String, statusCode: Int) -> Unit,
    onUnAuthorized:(message: String) -> Unit
) {
    onEach {response ->
        if (response.statusCode == HttpStatusCode.Unauthorized.value) {
            return@onEach onUnAuthorized(INVALID_AUTHENTICATION_TOKEN)
        }

        if (response is Response.ErrorResponse) {
            return@onEach onError(response.message ?: GENERIC_ERROR, response.statusCode)
        }

        response.data?.let { data ->
            onSuccess(data, response.token!!)
        } ?:
        response.message?.let {message ->
                return@onEach onError(message, response.statusCode)
        } ?: return@onEach onError(GENERIC_ERROR, response.statusCode)
    }.launchIn(scope)

}