package ui.login

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import data.Response
import domain.usecase.UserLoginUseCase
import kotlinx.coroutines.launch

class UserLoginViewModel(
    private val loginUseCase: UserLoginUseCase
): KMMViewModel() {

    fun login() {
        viewModelScope.coroutineScope.launch {
            val response = loginUseCase.invoke()
                if (response is Response.SuccessResponse) {
                    println("aaa ini sukses - $response")
                }else {
                    println("aaa ini error - $response")
                }
        }
    }

}