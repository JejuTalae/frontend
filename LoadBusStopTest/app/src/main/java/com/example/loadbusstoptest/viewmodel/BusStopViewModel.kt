package com.example.loadbusstoptest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loadbusstoptest.api.BusStopApi
import com.example.loadbusstoptest.model.BusStop
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BusStopViewModel : ViewModel() {
    private val _busStops = MutableLiveData<List<BusStop>>()
    val busStops: LiveData<List<BusStop>> = _busStops

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val api: BusStopApi = Retrofit.Builder()
        .baseUrl("https://api.example.com/") // 변경할 것
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BusStopApi::class.java)

    fun loadBusStops(busNumber: String) {
        viewModelScope.launch {
            try {
                val stops = if (busNumber == "360") {
                    listOf(
                        BusStop("1", "부영아파트", "제주시 노형동"),
                        BusStop("2", "한라중학교", "제주시 노형동"),
                        BusStop("3", "롯데마트", "제주시 노형동"),
                        BusStop("4", "노형오거리", "제주시 노형동"),
                        BusStop("5", "한라병원", "제주시 연동"),
                        BusStop("6", "제원아파트", "제주시 연동"),
                        BusStop("7", "은남동", "제주시 연동"),
                        BusStop("8", "제주도청(신제주로터리)", "제주시 연동"),
                        BusStop("9", "오라3동", "제주시 오라동"),
                        BusStop("10", "제주버스터미널", "제주시 오라동"),
                        BusStop("11", "남서광마을", "제주시 서광로"),
                        BusStop("12", "제주시청", "제주시 이도이동"),
                        BusStop("13", "제주여자중고등학교", "제주시 이도이동"),
                        BusStop("14", "아라초등학교", "제주시 아라일동"),
                        BusStop("15", "인다마을", "제주시 아라일동"),
                        BusStop("16", "제주대학교병원", "제주시 아라일동"),
                        BusStop("17", "제주대학교(영주고,국제대)", "제주시 아라일동")
                    )
                } else {
                    api.getBusStops(busNumber)
                }
                _busStops.value = stops
                _errorMessage.value = "" // 성공 시 에러 메시지 초기화
            } catch (e: Exception) {
                _errorMessage.value = "버스 정보를 불러오지 못했습니다: ${e.message}"
                _busStops.value = emptyList() // 에러 시 빈 리스트로 설정
            }
        }
    }
}