package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewEndpointDto;
import ru.practicum.repo.ViewStatsProjection;
import ru.practicum.util.exeptions.ValidationDateException;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.repo.EndpointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.util.exeptions.ExceptionMessages.*;

@Slf4j
@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class EndpointServiceImpl implements EndpointService {

    private final EndpointRepository repository;

    @Override
    @Transactional
    public EndpointHit add(EndpointHitDto endpointHitDto) {
        return repository.save(EndpointHitMapper.fromDto(endpointHitDto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewEndpointDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        if (start == null) throw new ValidationDateException(VALIDATOR_ERROR__START_DATETIME__IS_NULL);
        if (end == null) throw new ValidationDateException(VALIDATOR_ERROR__END_DATETIME__IS_NULL);
        if (end.isBefore(start) || start.isAfter(end)) throw new ValidationDateException(VALIDATOR_ERROR__NOT_VALID_DATETIME);

        List<ViewStatsProjection> results;
        if (unique) results = repository.findUniqueStats(start, end, uris);
        else results = repository.findNotUniqueStats(start, end, uris);

        return results.stream()
                .map(result -> ViewEndpointDto.builder()
                        .app(result.getApp())
                        .uri(result.getUri())
                        .hits(result.getHits())
                        .build()
                )
                .collect(Collectors.toList());
    }
}