package me.wadzapi.interview.service

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HamsterSensorSimulatorTest {

    val  testHamSimulator: HamsterSensorSimulator = HamsterSensorSimulator()

    @Test
    fun asyncHamLoopAsyncTest() = runBlocking {
        val count = 40
        val values = testHamSimulator.hamLoopAsyncWorkers(count)
        Assertions.assertEquals(values.joinToString(), (0 until count).joinToString())
        return@runBlocking
    }

}