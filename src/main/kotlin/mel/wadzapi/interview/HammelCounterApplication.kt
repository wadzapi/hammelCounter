package mel.wadzapi.interview

import kotlinx.coroutines.runBlocking
import mel.wadzapi.interview.service.HamsterSensorSimulator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class HammelCounterApplication

fun main(args: Array<String>) {
	val hamsterSimulator = HamsterSensorSimulator()
		runBlocking {
			hamsterSimulator.hamJob()
		}
	runApplication<HammelCounterApplication>(*args)
}
