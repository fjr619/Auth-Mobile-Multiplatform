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
    onError:(message: String) -> Unit,
    onUnAuthorized:(message: String) -> Unit
) {
    onEach {
        if (it.statusCode == HttpStatusCode.Unauthorized.value) {
            return@onEach onUnAuthorized(INVALID_AUTHENTICATION_TOKEN)
        }

        if (it is Response.ErrorResponse) {
            return@onEach onError(it.message ?: GENERIC_ERROR)
        }

        it.data?.let { data ->
            return@onEach onSuccess(data, it.token!!)
        } ?: return@onEach onError(GENERIC_ERROR)
    }.launchIn(scope)

}