package edu.mirea.onebeattrue.vktestpokemon.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import edu.mirea.onebeattrue.vktestpokemon.presentation.root.DefaultRootComponent
import edu.mirea.onebeattrue.vktestpokemon.presentation.root.RootContent
import edu.mirea.onebeattrue.vktestpokemon.ui.theme.VkTestPokemonTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as VkTestPokemonApp).component.inject(this)
        super.onCreate(savedInstanceState)

        val component = rootComponentFactory.create(defaultComponentContext())

        enableEdgeToEdge()
        setContent {
            VkTestPokemonTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    RootContent(component = component)
                }
            }
        }
    }
}