package mel.wadzapi.interview

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class HammelCounterApplication

fun main(args: Array<String>) {
	runApplication<HammelCounterApplication>(*args)
}
