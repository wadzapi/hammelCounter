package me.wadzapi.interview.service

import me.wadzapi.interview.entity.EventHappensDto
import me.wadzapi.interview.entity.HamStatResponseDto
import java.time.Instant

//TODO: javadocs && layering
interface StatTimerService {
    // Возвращает Instant на начало текущих суток
    fun today(): Instant?

    // Обработка входящего события
    fun processEvent(eventHappensDto: EventHappensDto)

    // Выдача статистики по идентификаторам
    fun calcHamStat(vararg hamsterIds: Int): HamStatResponseDto
}