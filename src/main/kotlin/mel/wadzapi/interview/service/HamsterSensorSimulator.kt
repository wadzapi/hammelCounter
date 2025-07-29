package mel.wadzapi.interview.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.util.Random

@Component
class HamsterSensorSimulator(val client: HttpClient = HttpClient (CIO)) {

    internal fun randomSeed(): IntArray {
        return Random(1).ints(1000000, 0, 1000)
            .toArray()
    }

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

    fun CoroutineScope.startInfiniteScheduler(interval: Long = 1000L, task: suspend () -> Unit): Job {
        return launch {
            while (isActive) {
                task()
                delay(interval)
            }
        }
    }

}