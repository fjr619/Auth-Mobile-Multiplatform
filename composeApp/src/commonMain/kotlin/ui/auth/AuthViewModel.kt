package ui.auth

import Platform
import Response
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import domain.extension.handleResult
import domain.model.User
import domain.usecase.user.UserLogin
import domain.usecase.user.UserRegister
import domain.usecase.user.UserUseCases
import getPlatform
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

data class UserUiState(
    val loading: Boolean = false,
    val user: User? = null,
    val message: String? = null,
    val token: String? = null
)

@Factory
class AuthViewModel(
    private val userUseCases: UserUseCases,
    private val logger: KLogger

) : ViewModel() {
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState = _uiState.asStateFlow()

    suspend fun login() {
        _uiState.update {
            it.copy(
                loading = true
            )
        }
        delay(500)
        userUseCases.userLogin.invoke().handleResult(
            viewModelScope,
            onSuccess = {user, token ->
                logger.info { "aaa ini sukses LOGIN : $user" }
                _uiState.update {
                    it.copy(
                        user = user,
                        message = null,
                        loading = false,
                        token = token
                    )
                }
            },
            onError = { message ->
                logger.debug { "aaa ini error LOGIN : $message"}
                _uiState.update {
                    it.copy(
                        user = null,
                        message = message,
                        loading = false
                    )
                }
            },
            onUnAuthorized = {
                logger.debug {"aaa ini unauthorized"}
            }
        )
    }

    suspend fun register() {
        _uiState.update {
            it.copy(
                loading = true
            )
        }

        delay(500)
        userUseCases.userRegister.invoke().handleResult(
            viewModelScope,
            onSuccess = {user, token ->
                _uiState.update {
                    it.copy(
                        user = user,
                        message = null,
                        loading = false,
                        token = token
                    )
                }
            },
            onError = { message ->
                println("aaa ini error REGISTER : $message")
                _uiState.update {
                    it.copy(
                        user = null,
                        message = message,
                        loading = false
                    )
                }
            },
            onUnAuthorized = {
                println("aaa ini unauthorized")
            }
        )
    }
}

