package edu.mirea.onebeattrue.vktestpokemon.presentation.extentions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

val ComponentContext.componentScope
    get() = CoroutineScope(
        Dispatchers.Main.immediate + SupervisorJob()
    ).apply {
        lifecycle.doOnDestroy { this.cancel() }
    }