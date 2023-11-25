package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewEndpointDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.service.EndpointService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.Constants.DATE_TEMPLATE;

@Slf4j
@Validated
@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatisticController {

    private final EndpointService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHit add(@Valid @RequestBody EndpointHitDto endpointHit) {
        log.info("  POST [http://localhost:9090/hit] : Сохранение информации о том, что был отправлен запрос к конечной точке");
        return service.save(endpointHit);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewEndpointDto> getStats(
            @RequestParam @DateTimeFormat(pattern = DATE_TEMPLATE) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DATE_TEMPLATE) LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique
    ) {
        log.info("\nGET [http://localhost:9090/stats] : Получение статистики по посещениям\n");
        return service.findStats(start, end, uris, unique);
    }
}