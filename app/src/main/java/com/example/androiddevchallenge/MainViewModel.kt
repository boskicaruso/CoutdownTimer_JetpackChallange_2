package com.example.androiddevchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.ICountdownRepository.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


const val START_VALUE: Long = 5000

class MainViewModel : ViewModel() {
    var simpleDateFormat: SimpleDateFormat = SimpleDateFormat("ss : SSS")

    private val countdownRepository = CountdownRepository()
    private var isTimerRunning = false
        set(value) {
            field = value
            _btnLabel.value = if (isTimerRunning) "Pause" else "Start"
        }

    private val _timerLiveData: MutableLiveData<String> = MutableLiveData()
    val timerLiveData: LiveData<String> = _timerLiveData

    private val _btnLabel: MutableLiveData<String> = MutableLiveData()
    val btnLabelLiveData: LiveData<String> = _btnLabel

    private var _timerValue: Long = START_VALUE
        set(value) {
            field = value
            _timerLiveData.value = simpleDateFormat.format(Date(_timerValue))
        }

    init {
        _timerValue = START_VALUE
        isTimerRunning = false
    }

    fun pressTimerBtn() {
        if (isTimerRunning) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    fun resetTimer() {

        val wasRunning = isTimerRunning
        if (isTimerRunning) {
            stopTimer()
        }
        _timerValue = START_VALUE
        if (wasRunning) {
            startTimer()
        }
    }


    private fun stopTimer() {
        countdownRepository.stop()
        isTimerRunning = false
    }

    private fun startTimer() {
        viewModelScope.launch {
            countdownRepository.start(getCurrentValue())
                .onStart { isTimerRunning = true }
                .collect { onTick ->
                    when (onTick) {
                        TimerState.Finished -> {
                            _timerValue = 0
                            isTimerRunning = false
                        }
                        is TimerState.OnTick -> {
                            _timerValue = onTick.tick
                        }
                    }
                }
        }
    }

    private fun getCurrentValue(): Long = if (_timerValue == 0L) {
        START_VALUE
    } else {
        _timerValue
    }
}