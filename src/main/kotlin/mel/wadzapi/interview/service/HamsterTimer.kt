package mel.wadzapi.interview.service

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


class HamsterTimer(
    val rng: Random = MersenneTwisterRNG(),
    val gen: NumberGenerator<Double> = ExponentialGenerator(6.0, rng)
) {
    fun hamFlow() = flow {
        val interval = (gen.nextValue() * ONE_MINUTE).roundToLong()
        delay(interval)

        //TODO: use logging instead
        println { "Ïts a hamFlow: ${(SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()))}" }
        //println("HamThread: Thead.name = ${Thread.currentThread()}")

        emit(HamsterEvent.HamsterEnter("hamId", "wheelId"))
    }

}