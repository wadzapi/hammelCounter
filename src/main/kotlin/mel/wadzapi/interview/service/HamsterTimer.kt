package mel.wadzapi.interview.service

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.selects.select
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.Random


@Component
/*
      //https://github.com/peter-lawrey/Java-Chronicle/blob/master/testing/src/main/java/com/higherfrequencytrading/chronicle/impl/BaseIndexedChronicleLatencyMain.java
      //https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-core/jvm/test/guide/example-channel-04.kt
      @ExperimentalCoroutinesApi
      @ObsoleteCoroutinesApi
*/
class HamsterTimer {

    internal fun randomSeed(): IntArray {
        return Random(1).ints(1000000, 0, 1000)
            .toArray()
    }

    suspend fun flowEventRandomized() {
        flow {
            emit(1)
            delay(50)

            emit(2)
            delay(50)

            emit(3)
        }
            .bufferTimeout(size = 500, duration = Duration.ofMillis(1000))
            .collect { println("Received batch: $it") }
    }



    fun <T> Flow<T>.bufferTimeout(size: Int, duration: Duration): Flow<List<T>> {
        require(size > 0) { "Window size should be greater than 0" }
        require(duration.toMillis() > 0) { "Duration should be greater than 0" }

        return flow {
            coroutineScope {
                val events = ArrayList<T>(size)
                val tickerChannel = ticker(duration.toMillis())
                try {
                    val upstreamValues = produce { collect { send(it) } }

                    while (isActive) {
                        var hasTimedOut = false

                        select<Unit> {
                            upstreamValues.onReceive {
                                events.add(it)
                            }

                            tickerChannel.onReceive {
                                hasTimedOut = true
                            }
                        }

                        if (events.size == size || (hasTimedOut && events.isNotEmpty())) {
                            emit(events.toList())
                            events.clear()
                        }
                    }
                } catch (e: ClosedReceiveChannelException) {
                    // drain remaining events
                    if (events.isNotEmpty()) emit(events.toList())
                } finally {
                    tickerChannel.cancel()
                }
            }
        }
    }

    fun <T> Flow<T>.chunked(chunkSize: Int): Flow<List<T>> {
        val buffer = mutableListOf<T>()
        return flow {
            this@chunked.collect {
                buffer.add(it)
                if (buffer.size == chunkSize) {
                    emit(buffer.toList())
                    buffer.clear()
                }
            }
            if (buffer.isNotEmpty()) {
                emit(buffer.toList())
            }
        }
    }

    fun CoroutineScope.startInfiniteScheduler(interval: Long = 1000L, task: suspend () -> Unit): Job {
        return launch {
            while (isActive) {
                task()
                delay(interval)
            }
        }
    }

}