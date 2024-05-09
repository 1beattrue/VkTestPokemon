package edu.mirea.onebeattrue.vktestpokemon.presentation.root

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import edu.mirea.onebeattrue.vktestpokemon.presentation.details.DetailsContent
import edu.mirea.onebeattrue.vktestpokemon.presentation.list.ListContent

@Composable
fun RootContent(
    modifier: Modifier = Modifier,
    component: RootComponent
) {
    Children(
        modifier = modifier,
        stack = component.stack,
        animation = stackAnimation(slide() + fade())
    ) {
        when (val instance = it.instance) {
            is RootComponent.Child.Details -> DetailsContent(component = instance.component)
            is RootComponent.Child.List -> ListContent(component = instance.component)
        }
    }
}