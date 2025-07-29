package mel.wadzapi.interview.service

import mel.wadzapi.interview.controller.dto.EventHappensDto
import mel.wadzapi.interview.controller.dto.HamStatResponseDto
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Collections
import java.util.HashMap
import java.util.HashSet
import java.util.Objects
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.IntStream

@Service
class HamStatService() : StatTimerService {
    private val userStat: MutableMap<Instant, MutableMap<Int, MutableSet<Int>>> = ConcurrentHashMap()
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
            .map { hamId: Int -> (lastDayMap!![hamId] ?: Collections.emptySet()).size }
            .toArray()
        return HamStatResponseDto(cnt)
    }

    @Scheduled(cron = "0 10 0 * * *", zone = "UTC")
    internal suspend fun statClean() {
        userStat.remove(today().minus(2, ChronoUnit.DAYS))

    }
}