package mel.wadzapi.interview.service

import io.klogging.Klogging
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import mel.wadzapi.interview.entity.HamsterEvent
import org.uncommons.maths.number.NumberGenerator
import org.uncommons.maths.random.ExponentialGenerator
import org.uncommons.maths.random.MersenneTwisterRNG
import java.text.SimpleDateFormat
import java.util.*
import javax.management.timer.Timer.ONE_MINUTE
import kotlin.math.roundToLong

//TODO: _low-latency_ AOF-based Redis sequence generator should be implemented instead
class HamsterTimer(
    val rng: Random = MersenneTwisterRNG(),
    val gen: NumberGenerator<Double> = ExponentialGenerator(6.0, rng)
) : Klogging {
    fun hamFlow() = flow {
        val interval = (gen.nextValue() * ONE_MINUTE).roundToLong()
        delay(interval)

        logger.debug { "Ïts a hamFlow: ${(SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()))}" }
        logger.trace {  "HamThread: Thead.name = ${Thread.currentThread()}" }

        emit(HamsterEvent.HamsterEnter("hamId", "wheelId"))
    }

}