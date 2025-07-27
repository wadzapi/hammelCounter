package mel.wadzapi.interview.service.impl

import mel.wadzapi.interview.controller.dto.EventHappensDto
import mel.wadzapi.interview.controller.dto.HamStatResponseDto
import mel.wadzapi.interview.service.StatTimerService
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.Collections.emptySet
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.IntStream

@Service
class HamStatService : StatTimerService {
    private val userStat: Map<Instant, MutableMap<Int, MutableSet<Int>>> = ConcurrentHashMap()
    override fun today(): Instant {
        return Instant.now().truncatedTo(ChronoUnit.DAYS)
    }

    override fun processEvent(eventHappensDto: EventHappensDto) {
        val userMapCounts = userStat[today()] ?: HashMap()
        userMapCounts.putIfAbsent(eventHappensDto.hamsterId, HashSet())
        userMapCounts[eventHappensDto.hamsterId]!!.add(eventHappensDto.wheelId)
    }

    override fun calcHamStat(vararg hamsterIds: Int): HamStatResponseDto {
        val lastDayMap: Map<Int, MutableSet<Int>>? =
            userStat[today().minus(1, ChronoUnit.DAYS)]
        if (Objects.isNull(lastDayMap)) return HamStatResponseDto(IntArray(0))
        val cnt = IntStream.of(*hamsterIds)
            .map { hamId: Int -> (lastDayMap!![hamId] ?: emptySet()).size }
            .toArray()
        return HamStatResponseDto(cnt)
    }
}