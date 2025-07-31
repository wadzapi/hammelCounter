package mel.wadzapi.interview.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class HamsterSensorSimulatorTest {

    val  testHamSimulator: HamsterSensorSimulator = HamsterSensorSimulator()

    @Test
    fun asyncHamLoopAsyncTest() = runBlocking {
        val count = 40
        val values = testHamSimulator.hamLoopAsync(count)
        assertThat(values.joinToString()).isEqualTo((0 until count).joinToString())
        return@runBlocking
    }

}