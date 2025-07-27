package mel.wadzapi.interview.controller

import mel.wadzapi.interview.controller.dto.EventHappensDto
import mel.wadzapi.interview.controller.dto.HamStatResponseDto
import mel.wadzapi.interview.service.StatTimerService
import org.springframework.web.bind.annotation.*

@RestController
class EventController(private val statTimerService: StatTimerService) {
    @PostMapping("/event")
    fun postEvent(@RequestBody event: EventHappensDto) {
        statTimerService.processEvent(event)
    }

    @GetMapping("/ham")
    fun getHamStat(@RequestParam ids: IntArray): HamStatResponseDto {
        return statTimerService.calcHamStat(*ids)
    }
}