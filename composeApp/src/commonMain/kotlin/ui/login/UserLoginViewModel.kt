package ui.login

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import Response
import domain.usecase.UserLoginUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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