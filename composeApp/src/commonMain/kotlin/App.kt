import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import domain.extension.pretty
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.getKoin
import ui.auth.AuthViewModel
import ui.auth.UserUiState

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(viewModel: AuthViewModel = getKoin().get<AuthViewModel>()) {
    MaterialTheme {
        val composableScope = rememberCoroutineScope()
        val uiState by viewModel.uiState.collectAsState()
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                composableScope.launch {
                    viewModel.register()
                }
            }) {
                Text("Register!")
            }

            Button(onClick = {
                composableScope.launch {
                    viewModel.login()
                }
            }) {
                Text("Login!")
            }

            AnimatedVisibility(visible = !uiState.loading) {
                Text("Compose: ${uiState.pretty()}")
            }


        }
    }
}

