/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.MyTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        TimerScreen()
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}


@Composable
fun TimerScreen(mainViewModel: MainViewModel = MainViewModel()) {
    val name: String by mainViewModel.timerLiveData.observeAsState("")
    val btnLabel: String by mainViewModel.btnLabelLiveData.observeAsState("")
    Timer(
        tick = name,
        btnLabel = btnLabel,
        onStartStop = { mainViewModel.pressTimerBtn() },
        onReset = { mainViewModel.resetTimer() })
}

@Composable
fun Timer(tick: String, btnLabel: String, onStartStop: () -> Unit, onReset: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = tick,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(CenterHorizontally),
            style = MaterialTheme.typography.h5
        )

        Row(
            modifier = Modifier
                .align(CenterHorizontally)
        ) {

            Button(
                onClick = onStartStop,
                modifier = Modifier.width(80.dp)
            ) {
                Text(btnLabel)
            }

            Spacer(modifier = Modifier.width(20.dp))

            Button(
                onClick = onReset,
                modifier = Modifier
                    .width(80.dp)
            ) {
                Text("Reset")
            }
        }
    }
}
