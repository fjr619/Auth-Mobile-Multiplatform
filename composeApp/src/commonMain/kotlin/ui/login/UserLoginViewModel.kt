package ui.login

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import data.Response
import domain.usecase.UserLoginUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.log

class UserLoginViewModel(
    private val loginUseCase: UserLoginUseCase
): KMMViewModel() {
    suspend fun login() {
        loginUseCase.invoke().onEach { response ->
                if (response is Response.SuccessResponse) {
                    println("aaa ini sukses - $response")
                }else {
                    println("aaa ini error - $response")
                }
        }.launchIn(viewModelScope.coroutineScope)
    }

}