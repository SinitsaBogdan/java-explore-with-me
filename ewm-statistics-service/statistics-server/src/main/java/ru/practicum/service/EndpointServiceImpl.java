package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewEndpointDto;
import ru.practicum.util.exeptions.ValidationDateException;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.repo.EndpointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.exeptions.ExceptionMessages.VALIDATOR_ERROR__NOT_VALID_DATETIME;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EndpointServiceImpl implements EndpointService {

    private final EndpointRepository repository;

    @Override
    @Transactional
    public EndpointHit save(EndpointHitDto endpointHitDto) {
        return repository.save(EndpointHitMapper.INSTANCE.fromDto(endpointHitDto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewEndpointDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        if (end.isBefore(start) || start.isAfter(end)) {
            throw new ValidationDateException(VALIDATOR_ERROR__NOT_VALID_DATETIME);
        }

        if (unique) return repository.findUniqueEndpoint(start, end, uris);
        else return repository.findEndpoint(start, end, uris);
    }
}
