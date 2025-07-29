package mel.wadzapi.interview.service

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import org.springframework.stereotype.Component
import java.util.Random


@Component
class HamsterTimer() {

    internal fun randomSeed(): IntArray {
        return Random(1).ints(1000000, 0, 1000)
            .toArray()
    }

    fun someCo(delay: Long) {
        GlobalScope.launch {
            while (isActive) {
                delay(delay)
                println("One more: Thead.name = ${Thread.currentThread()}")
                yield()
            }
        }
    }

    fun flowEventRandomized(period: Long, initialDelay: Long = 0L) = flow {
        delay(initialDelay)
        while (true) {
            emit(Unit)
            delay(period)
        }
    }

}