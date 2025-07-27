package mel.wadzapi.interview.service

import mel.wadzapi.interview.controller.dto.EventHappensDto
import mel.wadzapi.interview.controller.dto.HamStatResponseDto
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