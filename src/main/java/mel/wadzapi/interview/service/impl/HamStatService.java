package mel.wadzapi.interview.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

import mel.wadzapi.interview.controller.dto.EventHappensDto;
import mel.wadzapi.interview.controller.dto.HamStatResponseDto;
import mel.wadzapi.interview.service.StatTimerService;
import org.springframework.stereotype.Service;

@Service
public class HamStatService implements StatTimerService {

    private Map<Instant, Map<Integer, Set<Integer>>> userStat = new ConcurrentHashMap<>();

    @Override
    public Instant today() {
        return Instant.now().truncatedTo(ChronoUnit.DAYS);
    }

    public void processEvent(EventHappensDto eventHappensDto) {
        var userMapCounts = userStat.getOrDefault(today(), new HashMap<>());
        userMapCounts.putIfAbsent(eventHappensDto.hamsterId(), new HashSet<>());
        userMapCounts.get(eventHappensDto.hamsterId()).add(eventHappensDto.wheelId());
    }

    public HamStatResponseDto calcHamStat(int... hamsterIds) {
        var lastDayMap = userStat.get(today().minus(1, ChronoUnit.DAYS));
        if (Objects.isNull(lastDayMap)) return new HamStatResponseDto(new int[0]);
        var cnt = IntStream.of(hamsterIds)
                .map(hamId -> lastDayMap.getOrDefault(hamId, Collections.emptySet()).size())
                .toArray();
        return new HamStatResponseDto(cnt);
    }

}
