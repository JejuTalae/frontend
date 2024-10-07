package com.example.loadbusstoptest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loadbusstoptest.model.BusStop
import com.example.loadbusstoptest.ui.theme.LoadBusStopTestTheme
import com.example.loadbusstoptest.viewmodel.BusStopViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoadBusStopTestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BusStopScreen()
                }
            }
        }
    }
}

@Composable
fun BusStopScreen(viewModel: BusStopViewModel = viewModel()) {
    var busNumber by remember { mutableStateOf("") }
    val busStops by viewModel.busStops.observeAsState(initial = emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState(initial = "")

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = busNumber,
            onValueChange = { busNumber = it },
            label = { Text("버스 번호") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { viewModel.loadBusStops(busNumber) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("정거장 정보 불러오기")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            BusStopRoute(busStops = busStops)
        }
    }
}

@Composable
fun BusStopRoute(busStops: List<BusStop>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        busStops.forEachIndexed { index, busStop ->
            BusStopItem(busStop = busStop, isLast = index == busStops.size - 1)
        }
    }
}

@Composable
fun BusStopItem(busStop: BusStop, isLast: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(Color.Blue, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = busStop.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = busStop.location, style = MaterialTheme.typography.bodyMedium)
        }
    }
    if (!isLast) {
        Box(
            modifier = Modifier
                .padding(start = 7.dp)
                .width(2.dp)
                .height(24.dp)
                .background(Color.Gray)
        )
    }
}