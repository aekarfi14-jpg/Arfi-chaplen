package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.AudioManager
import android.media.ToneGenerator
import com.example.data.model.AlgerianMusicTrack
import kotlinx.coroutines.*
import kotlin.math.*
import kotlin.random.Random

/**
 * Algerian Sound & Procedural Music Engine for Arfi Chaplen.
 * Provides authentic procedural Algerian music styles (Rap-Raï, Raï Moderne, Chaâbi, Gnawa)
 * and dynamic game sound effects with zero external audio assets.
 */
class SoundManager(private val context: Context) {

    private val audioScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var musicJob: Job? = null

    var sfxEnabled: Boolean = true
    var musicEnabled: Boolean = true
        set(value) {
            field = value
            if (!value) {
                stopMenuMusic()
            }
        }

    var currentTrack: AlgerianMusicTrack = AlgerianMusicTrack.RAP_RAI
        set(value) {
            val changed = field != value
            field = value
            if (changed && musicEnabled && musicJob?.isActive == true) {
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

    // --- Algerian Music Procedural Engine ---

    fun startMenuMusic(track: AlgerianMusicTrack = currentTrack) {
        currentTrack = track
        if (!musicEnabled) return
        if (musicJob?.isActive == true) {
            musicJob?.cancel()
        }

        musicJob = audioScope.launch {
            when (track) {
                AlgerianMusicTrack.RAP_RAI -> playRapRaiLoop()
                AlgerianMusicTrack.RAI_ARASSI -> playRaiArassiLoop()
                AlgerianMusicTrack.CHAABI_ALGEROIS -> playChaabiLoop()
                AlgerianMusicTrack.GNAWA_DIWAN -> playGnawaLoop()
            }
        }
    }

    fun stopMenuMusic() {
        musicJob?.cancel()
        musicJob = null
    }

    /**
     * 1. Rap-Raï Loop (808 Trap Bass + Bayati Synth Lead + Hi-hats)
     */
    private suspend fun CoroutineScope.playRapRaiLoop() {
        // D minor / Bayati scale notes: D4, Eb4, F4, G4, A4, Bb4, C5, D5
        val melodyPatterns = listOf(
            listOf(293.66, 311.13, 349.23, 392.00, 349.23, 311.13, 293.66, 293.66),
            listOf(392.00, 440.00, 466.16, 440.00, 392.00, 349.23, 311.13, 293.66),
            listOf(440.00, 466.16, 523.25, 466.16, 440.00, 392.00, 349.23, 311.13),
            listOf(293.66, 349.23, 392.00, 311.13, 293.66, 261.63, 293.66, 293.66)
        )
        val bassNotes = listOf(146.83, 155.56, 174.61, 146.83) // D3, Eb3, F3, D3

        var beatIndex = 0
        while (isActive && musicEnabled) {
            val pattern = melodyPatterns[beatIndex % melodyPatterns.size]
            val currentBass = bassNotes[beatIndex % bassNotes.size]

            // Play 808 Sub-bass kick
            playSyntheticTone(currentBass, 140, WaveType.SINE, volume = 0.14f)

            for ((step, note) in pattern.withIndex()) {
                if (!isActive || !musicEnabled) break

                // Synth lead note with subtle autotune glide feeling
                val synthType = if (step % 2 == 0) WaveType.SAWTOOTH else WaveType.TRIANGLE
                playSyntheticTone(note, 90, synthType, volume = 0.08f)

                // Snare / clap on beat 2 and 4 (step 2 and 6)
                if (step == 2 || step == 6) {
                    playSyntheticNoise(durationMs = 45, volume = 0.06f)
                }

                // Hi-hat tick
                if (step % 2 == 1) {
                    playSyntheticTone(2400.0, 15, WaveType.SQUARE, volume = 0.03f)
                }

                delay(120) // ~125 BPM
            }
            beatIndex++
            delay(40)
        }
    }

    /**
     * 2. Raï Arassi Loop (6/8 galloping Gallal rhythm + Korg Synth Lead)
     */
    private suspend fun CoroutineScope.playRaiArassiLoop() {
        // Vibrant celebratory Raï motifs in G minor / Bayati
        val raiPhrases = listOf(
            listOf(392.00, 415.30, 466.16, 523.25, 466.16, 415.30),
            listOf(523.25, 587.33, 622.25, 587.33, 523.25, 466.16),
            listOf(466.16, 523.25, 466.16, 415.30, 392.00, 349.23),
            listOf(392.00, 349.23, 392.00, 415.30, 392.00, 392.00)
        )

        var phraseIndex = 0
        while (isActive && musicEnabled) {
            val phrase = raiPhrases[phraseIndex % raiPhrases.size]

            // 6/8 Gallal Drum rhythm: Dum (0), Tak (2), Tak (3), Dum (4), Tak (5)
            for (step in 0 until 6) {
                if (!isActive || !musicEnabled) break

                val note = phrase[step]
                // Lead Korg synth tone
                playSyntheticTone(note, 85, WaveType.SAWTOOTH, volume = 0.08f)

                // Gallal / Darbuka accents
                when (step) {
                    0, 4 -> playSyntheticTone(110.0, 60, WaveType.SINE, volume = 0.12f) // Dum
                    2, 3, 5 -> playSyntheticTone(1200.0, 25, WaveType.TRIANGLE, volume = 0.04f) // Tak
                }

                delay(110) // 6/8 fast cadence (~136 BPM)
            }
            phraseIndex++
            delay(20)
        }
    }

    /**
     * 3. Chaâbi Algérois Loop (Mandole arpeggios + Derbouka / Tar)
     */
    private suspend fun CoroutineScope.playChaabiLoop() {
        // Traditional Algiers Casbah progression (D minor, C, Bb, A)
        val mandoleChords = listOf(
            listOf(293.66, 349.23, 440.00, 587.33), // Dm
            listOf(261.63, 329.63, 392.00, 523.25), // C
            listOf(233.08, 293.66, 349.23, 466.16), // Bb
            listOf(220.00, 277.18, 329.63, 440.00)  // A
        )

        var chordIdx = 0
        while (isActive && musicEnabled) {
            val chord = mandoleChords[chordIdx % mandoleChords.size]

            // Bass strum
            playSyntheticTone(chord[0] / 2.0, 120, WaveType.TRIANGLE, volume = 0.11f)

            for (note in chord) {
                if (!isActive || !musicEnabled) break
                // Strummed mandole acoustic string simulation
                playSyntheticTone(note, 75, WaveType.TRIANGLE, volume = 0.07f)
                // Subtle derbouka tap
                playSyntheticTone(800.0, 20, WaveType.SINE, volume = 0.03f)
                delay(130)
            }

            // Return strum
            playSyntheticTone(chord[2], 80, WaveType.TRIANGLE, volume = 0.06f)
            delay(100)

            chordIdx++
            delay(30)
        }
    }

    /**
     * 4. Gnawa & Diwan Loop (Guembri bass riff + Qraqeb polyrhythms)
     */
    private suspend fun CoroutineScope.playGnawaLoop() {
        // Pentatonic desert groove in G: G2, Bb2, C3, D3, F3
        val guembriRiff = listOf(
            98.00, 98.00, 116.54, 130.81, 146.83, 130.81, 116.54, 98.00,
            130.81, 146.83, 174.61, 146.83, 130.81, 116.54, 98.00, 98.00
        )

        var stepIdx = 0
        while (isActive && musicEnabled) {
            val bassFreq = guembriRiff[stepIdx % guembriRiff.size]

            // Deep Guembri pluck with resonant body
            playSyntheticTone(bassFreq, 110, WaveType.SINE, volume = 0.16f)

            // Qraqeb metallic double clack (shik-shik-chak)
            if (stepIdx % 2 == 0) {
                playSyntheticTone(1800.0, 20, WaveType.SQUARE, volume = 0.04f)
            } else {
                playSyntheticTone(2200.0, 15, WaveType.SQUARE, volume = 0.03f)
            }

            stepIdx++
            delay(125) // ~120 BPM
        }
    }

    // --- Audio Synthesis Utilities ---

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
