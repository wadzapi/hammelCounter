package me.wadzapi.interview.entity

// События от датчиков
sealed class HamsterEvent {
data class WheelSpin(val wheelId: String, val durationMs: Long) : HamsterEvent()
data class HamsterEnter(val hamsterId: String, val wheelId: String) : HamsterEvent()
data class HamsterExit(val hamsterId: String, val wheelId: String) : HamsterEvent()
data class SensorFailure(val sensorId: String, val errorCode: Int) : HamsterEvent()
}