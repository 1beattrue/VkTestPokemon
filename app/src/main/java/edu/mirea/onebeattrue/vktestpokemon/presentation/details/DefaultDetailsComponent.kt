package edu.mirea.onebeattrue.vktestpokemon.presentation.details

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import edu.mirea.onebeattrue.vktestpokemon.domain.entity.Pokemon
import edu.mirea.onebeattrue.vktestpokemon.presentation.extentions.componentScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DefaultDetailsComponent @AssistedInject constructor(
    @Assisted("pokemon") override val pokemon: Pokemon,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext,
) : DetailsComponent, ComponentContext by componentContext {

    private var soundJob: Job? = null
    private val mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }
    private val handler = CoroutineExceptionHandler { _, e ->
        Log.e("DefaultDetailsComponent", "$e")
    }

    override fun onClickBack() {
        onBackClicked()
    }

    override fun playCry() {
        soundJob?.cancel()
        soundJob = componentScope.launch(Dispatchers.IO + handler) {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(pokemon.cryUrl)
            mediaPlayer.prepare()
            mediaPlayer.start()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("pokemon") pokemon: Pokemon,
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultDetailsComponent
    }
}