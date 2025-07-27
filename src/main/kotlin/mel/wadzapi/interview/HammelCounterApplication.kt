package mel.wadzapi.interview

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HammelCounterApplication

fun main(args: Array<String>) {
	runApplication<HammelCounterApplication>(*args)
}
