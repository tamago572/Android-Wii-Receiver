package com.bunbun.irlightsensor

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.ConsumerIrManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bunbun.irlightsensor.ui.theme.IRLightSensorTheme
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

var canLaunching = false

class MainActivity : ComponentActivity() {
    @SuppressLint("ServiceCast")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IRLightSensorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        Greeting(
                            name = "Xiaomi Android",
                            modifier = Modifier.padding(innerPadding)
                        )
                        SwitchIR()
                    }
                }
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun SwitchIR() {
    val context = LocalContext.current
    val consumerIrManager = context.getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager
    var isStartedIR by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val IRList: IntArray = intArrayOf(1000)
    var irJob by remember { mutableStateOf<Job?>(null) }


    Button(onClick = {
        isStartedIR = !isStartedIR

        if (consumerIrManager.hasIrEmitter()) {
            // ある場合
            Log.d("Develop", "IRあります")
        } else {
            // 無い場合
            Log.d("Develop", "IRないです")
        }

        if (isStartedIR) {
            irJob = scope.launch {
                while (true) {
                    Log.d("Develop", "照射中")
                    consumerIrManager.transmit(38000, IRList)
                    delay(10)
                }
            }

        } else {
            irJob?.cancel()
            irJob = null
            Log.d("Develop", "照射停止")
        }


    }) {
        Text(if (!isStartedIR) "センサーを起動" else "センサーを停止")
    }

    Text("状態: ${isStartedIR}")
}

suspend fun wait10sec() {
    delay(10000)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IRLightSensorTheme {
        Greeting("Android")
    }
}