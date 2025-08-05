package me.wadzapi.interview.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsText
import io.vertx.core.AbstractVerticle
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class HamsterSensorSimulator(hamsterCount : Long = 1,
                             sensorCount : Long = 1) : AbstractVerticle() {
    companion object {
        const val MIN_COUNT = 1;
        const val MAX_COUNT = 10000;
        const val WORKER_HAMSTERS_THRESHOLD = 10;
    }
    //TODO: workers (launchWorker)
    //https://gist.github.com/jivimberg/b0f4f94871c6f3e7d17fae1106c28047
    private val timer : HamsterTimer = HamsterTimer()

    val client: HttpClient = HttpClient (CIO)

    suspend fun getHttp(reqUrl : String): String {
        return client.prepareGet(reqUrl).execute { response ->
            response.bodyAsText()
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

    suspend fun hamLoopAsyncWorkers(count : Int) : List<Job> = coroutineScope {
        buildList<Deferred<Job>>(capacity = count) {
            for (n in 0 until count) async(Dispatchers.Default) {
                launchHamJobFlow()
            }
        }.awaitAll()
    }

    //async vs launch test
    suspend fun launchHamJobFlow(): Job = coroutineScope {
         launch {
            while (isActive) {
                timer.hamFlow().collect { value -> println(value) }
                delay(1000L)
            }
        }
    }

}