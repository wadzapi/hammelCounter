package me.wadzapi.interview.service.impl

import io.klogging.Klogging
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import me.wadzapi.interview.entity.EventHappensDto
import me.wadzapi.interview.entity.HamStatResponseDto
import me.wadzapi.interview.service.StatTimerService
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Collections
import java.util.HashMap
import java.util.HashSet
import java.util.Objects
import java.util.Timer
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.IntStream
import kotlin.concurrent.timerTask

class HamStatService() : AbstractVerticle(), StatTimerService, Klogging {
    private val userStat: MutableMap<Instant, MutableMap<Int, MutableSet<Int>>> = ConcurrentHashMap()

    override fun start(promise: Promise<Void>) {
        startupCleaner()
    }

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

    internal fun startupCleaner() = Timer().scheduleAtFixedRate(
        timerTask {
            userStat.remove(today().minus(2, ChronoUnit.DAYS))
        },
        0L,
        CLEANUP_INTERVAL
    )

    companion object {
        const val CLEANUP_INTERVAL = 86400000L

    }
}