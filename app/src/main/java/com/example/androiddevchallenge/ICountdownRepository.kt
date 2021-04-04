package com.example.androiddevchallenge

import android.os.CountDownTimer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface ICountdownRepository {

    fun start(startTime: Long): Flow<TimerState>

    fun stop()

    sealed class TimerState {
        data class OnTick(val tick: Long) : TimerState()
        object Finished : TimerState()
    }
}

@ExperimentalCoroutinesApi
class CountdownRepository : ICountdownRepository {

    private var countDownTimer: CountDownTimer? = null

    override fun start(startTime: Long) = callbackFlow<ICountdownRepository.TimerState> {

        countDownTimer = object : CountDownTimer(startTime, 10) {
            override fun onTick(millisUntilFinished: Long) {
                offer(ICountdownRepository.TimerState.OnTick(millisUntilFinished))
            }

            override fun onFinish() {
                offer(ICountdownRepository.TimerState.Finished)
                close()
            }
        }.start()

        awaitClose {  }
    }

    override fun stop() {
        countDownTimer?.cancel()
    }
}