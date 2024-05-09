package edu.mirea.onebeattrue.vktestpokemon.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import edu.mirea.onebeattrue.vktestpokemon.presentation.details.DetailsComponent
import edu.mirea.onebeattrue.vktestpokemon.presentation.list.ListComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        class Details(val component: DetailsComponent) : Child
        class List(val component: ListComponent) : Child
    }
}