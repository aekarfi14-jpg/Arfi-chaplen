package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.ToneGenerator
import com.example.R
import com.example.data.model.AlgerianMusicTrack
import kotlinx.coroutines.*
import kotlin.math.*
import kotlin.random.Random

/**
 * Algerian Sound & Music Engine for Arfi Chaplen.
 * Plays bundled local MP3 audio assets for the 4 Algerian music genres:
 * - RAP_RAI -> sirocco_velocity.mp3
 * - RAI_ARASSI -> music-victory.mp3 / music_victory.mp3
 * - CHAABI_ALGEROIS -> arfi_chaplen.mp3
 * - GNAWA_DIWAN -> copper_coin_carousel.mp3
 *
 * Also provides dynamic game sound effects and tone feedback.
 */
class SoundManager(private val context: Context) {

    private val audioScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var mediaPlayer: MediaPlayer? = null

    var sfxEnabled: Boolean = true
    var musicEnabled: Boolean = true
        set(value) {
            field = value
            if (!value) {
                stopMenuMusic()
            } else {
                startMenuMusic(currentTrack)
            }
        }

    var currentTrack: AlgerianMusicTrack = AlgerianMusicTrack.RAP_RAI
        set(value) {
            val changed = field != value
            field = value
            if (changed && musicEnabled && mediaPlayer != null) {
                // Restart with new track
                startMenuMusic(value)
            }
        }

    private var toneGen: ToneGenerator? = null

    init {
        try {
            toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
        } catch (e: Exception) {
            toneGen = null
        }
    }

    // --- SFX Section ---

    fun playButtonClick() {
        if (!sfxEnabled) return
        playSyntheticTone(frequency = 700.0, durationMs = 35, type = WaveType.SINE, volume = 0.25f)
    }

    fun playCorrect() {
        if (!sfxEnabled) return
        audioScope.launch {
            // Bright cheerful ascending arpeggio: C5 -> E5 -> G5 -> C6 with sparkle
            playSyntheticTone(523.25, 55, WaveType.TRIANGLE, 0.35f)
            delay(45)
            playSyntheticTone(659.25, 55, WaveType.TRIANGLE, 0.35f)
            delay(45)
            playSyntheticTone(783.99, 65, WaveType.TRIANGLE, 0.38f)
            delay(55)
            playSyntheticTone(1046.50, 160, WaveType.SINE, 0.40f)
        }
    }

    fun playSkip() {
        if (!sfxEnabled) return
        audioScope.launch {
            // Descending swift penalty whoosh: 440Hz -> 280Hz
            playSyntheticTone(440.0, 65, WaveType.SINE, 0.30f)
            delay(35)
            playSyntheticTone(280.0, 110, WaveType.SINE, 0.28f)
        }
    }

    fun playBadPerformance() {
        if (!sfxEnabled) return
        audioScope.launch {
            // Comic fail buzzer
            playSyntheticTone(311.13, 90, WaveType.SAWTOOTH, 0.32f)
            delay(70)
            playSyntheticTone(207.65, 200, WaveType.SAWTOOTH, 0.35f)
        }
    }

    fun playTurnStart() {
        if (!sfxEnabled) return
        audioScope.launch {
            // Energetic whistle & fanfare
            playSyntheticTone(880.0, 80, WaveType.SINE, 0.35f)
            delay(60)
            playSyntheticTone(1174.66, 150, WaveType.TRIANGLE, 0.38f)
        }
    }

    fun playTurnEnd() {
        if (!sfxEnabled) return
        audioScope.launch {
            // Buzzer horn
            playSyntheticTone(261.63, 180, WaveType.SQUARE, 0.32f)
            delay(90)
            playSyntheticTone(220.00, 320, WaveType.SQUARE, 0.35f)
        }
    }

    fun playCountdownTick(secondsRemaining: Int) {
        if (!sfxEnabled) return
        val freq = when (secondsRemaining) {
            5 -> 650.0
            4 -> 750.0
            3 -> 850.0
            2 -> 980.0
            1 -> 1200.0
            else -> 600.0
        }
        playSyntheticTone(freq, 50, WaveType.SINE, 0.30f)
    }

    fun playRandomEventTrigger() {
        if (!sfxEnabled) return
        audioScope.launch {
            // Meme comic fanfare
            playSyntheticTone(440.0, 65, WaveType.SQUARE, 0.30f)
            delay(45)
            playSyntheticTone(554.37, 65, WaveType.SQUARE, 0.30f)
            delay(45)
            playSyntheticTone(659.25, 65, WaveType.SQUARE, 0.30f)
            delay(55)
            playSyntheticTone(880.00, 220, WaveType.TRIANGLE, 0.35f)
        }
    }

    fun playVictory() {
        if (!sfxEnabled) return
        audioScope.launch {
            val notes = listOf(523.25, 659.25, 783.99, 1046.50, 880.00, 1046.50, 1318.51)
            for (note in notes) {
                playSyntheticTone(note, 110, WaveType.TRIANGLE, 0.35f)
                delay(100)
            }
        }
    }

    // --- Algerian Music Playback Engine (Bundled Local MP3 Audio Assets) ---

    fun startMenuMusic(track: AlgerianMusicTrack = currentTrack) {
        currentTrack = track
        if (!musicEnabled) return

        stopMenuMusic()

        try {
            val rawResId = when (track) {
                AlgerianMusicTrack.RAP_RAI -> R.raw.sirocco_velocity
                AlgerianMusicTrack.RAI_ARASSI -> R.raw.music_victory
                AlgerianMusicTrack.CHAABI_ALGEROIS -> R.raw.arfi_chaplen
                AlgerianMusicTrack.GNAWA_DIWAN -> R.raw.copper_coin_carousel
            }

            mediaPlayer = MediaPlayer.create(context, rawResId)?.apply {
                isLooping = true
                setVolume(0.75f, 0.75f)
                start()
            }
        } catch (e: Exception) {
            // Graceful fallback attempt using assets
            try {
                val assetFileName = when (track) {
                    AlgerianMusicTrack.RAP_RAI -> "music/sirocco_velocity.mp3"
                    AlgerianMusicTrack.RAI_ARASSI -> "music/music-victory.mp3"
                    AlgerianMusicTrack.CHAABI_ALGEROIS -> "music/arfi_chaplen.mp3"
                    AlgerianMusicTrack.GNAWA_DIWAN -> "music/copper_coin_carousel.mp3"
                }
                val afd = context.assets.openFd(assetFileName)
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                    afd.close()
                    isLooping = true
                    setVolume(0.75f, 0.75f)
                    prepare()
                    start()
                }
            } catch (fallbackEx: Exception) {
                mediaPlayer = null
            }
        }
    }

    fun stopMenuMusic() {
        try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
                player.reset()
                player.release()
            }
        } catch (e: Exception) {
            // Ignore clean up errors
        } finally {
            mediaPlayer = null
        }
    }

    // --- Audio Synthesis Utilities for SFX ---

    private enum class WaveType { SINE, SQUARE, TRIANGLE, SAWTOOTH }

    private fun playSyntheticTone(
        frequency: Double,
        durationMs: Int,
        type: WaveType,
        volume: Float = 0.35f
    ) {
        audioScope.launch {
            try {
                val sampleRate = 22050
                val numSamples = (sampleRate * (durationMs / 1000.0)).toInt().coerceAtLeast(1)
                val buffer = ShortArray(numSamples)

                val angularFrequency = 2.0 * Math.PI * frequency / sampleRate

                for (i in 0 until numSamples) {
                    val angle = angularFrequency * i
                    val rawSample = when (type) {
                        WaveType.SINE -> sin(angle)
                        WaveType.SQUARE -> if (sin(angle) >= 0) 0.7 else -0.7
                        WaveType.TRIANGLE -> {
                            val t = (angle / (2 * Math.PI)) % 1.0
                            if (t < 0.5) 4.0 * t - 1.0 else 3.0 - 4.0 * t
                        }
                        WaveType.SAWTOOTH -> {
                            val t = (angle / (2 * Math.PI)) % 1.0
                            2.0 * t - 1.0
                        }
                    }

                    // ADSR Envelope
                    val attack = (numSamples * 0.10).toInt().coerceAtLeast(1)
                    val release = (numSamples * 0.20).toInt().coerceAtLeast(1)
                    val envelope = when {
                        i < attack -> i.toDouble() / attack
                        i > numSamples - release -> (numSamples - i).toDouble() / release
                        else -> 1.0
                    }

                    val sampleValue = (rawSample * Short.MAX_VALUE * volume * envelope).toInt()
                    buffer[i] = sampleValue.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(buffer, 0, buffer.size)
                audioTrack.play()
                delay(durationMs.toLong() + 20)
                audioTrack.stop()
                audioTrack.release()
            } catch (e: Exception) {
                // Ignore audio errors gracefully
            }
        }
    }

    private fun playSyntheticNoise(durationMs: Int, volume: Float = 0.05f) {
        audioScope.launch {
            try {
                val sampleRate = 22050
                val numSamples = (sampleRate * (durationMs / 1000.0)).toInt().coerceAtLeast(1)
                val buffer = ShortArray(numSamples)
                val random = Random.Default

                for (i in 0 until numSamples) {
                    val envelope = (numSamples - i).toDouble() / numSamples
                    val noise = (random.nextDouble() * 2.0 - 1.0)
                    val sampleValue = (noise * Short.MAX_VALUE * volume * envelope).toInt()
                    buffer[i] = sampleValue.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }

                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(buffer, 0, buffer.size)
                audioTrack.play()
                delay(durationMs.toLong() + 15)
                audioTrack.stop()
                audioTrack.release()
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    fun release() {
        stopMenuMusic()
        try {
            toneGen?.release()
        } catch (e: Exception) {}
    }
}

