package ui.login

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.coroutineScope
import domain.usecase.UserLoginUseCase
import kotlinx.coroutines.launch

class UserLoginViewModel(
    private val loginUseCase: UserLoginUseCase
): KMMViewModel() {

    fun login() {
        viewModelScope.coroutineScope.launch {
            loginUseCase.invoke()
                .onSuccess {
                    println("aaa ini sukses - $it")
                }
                .onFailure {
                    println("aaa ini gagal- ${it.message}")
                }
        }
    }

}