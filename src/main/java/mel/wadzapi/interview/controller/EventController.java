package mel.wadzapi.interview.controller;

import mel.wadzapi.interview.controller.dto.EventHappensDto;
import mel.wadzapi.interview.controller.dto.HamStatResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import mel.wadzapi.interview.service.StatTimerService;

@RestController
public class EventController {
    private final StatTimerService statTimerService;

    public EventController(StatTimerService statTimerService) {
        this.statTimerService = statTimerService;
    }

    @PostMapping("/event")
    public void postEvent(@RequestBody EventHappensDto event) {
        statTimerService.processEvent(event);
    }

    @GetMapping("/ham")
    public HamStatResponseDto getHamStat(@RequestParam int[] ids) {
        return statTimerService.calcHamStat(ids);
    }
}
