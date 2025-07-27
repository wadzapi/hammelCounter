package mel.wadzapi.interview.service;

import mel.wadzapi.interview.controller.dto.EventHappensDto;
import mel.wadzapi.interview.controller.dto.HamStatResponseDto;

import java.time.Instant;

//TODO: javadocs && layering
public interface StatTimerService {
    // Возвращает Instant на начало текущих суток
    Instant today();
    // Обработка входящего события
    void processEvent(EventHappensDto eventHappensDto);
    // Выдача статистики по идентификаторам
    HamStatResponseDto calcHamStat(int... hamsterIds);

}
