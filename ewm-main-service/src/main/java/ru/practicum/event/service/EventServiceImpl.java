package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.category.model.Category;
import ru.practicum.category.repo.CategoryRepository;
import ru.practicum.client.StatisticsClient;
import ru.practicum.dto.ViewEndpointDto;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repo.EventRepository;
import ru.practicum.event.util.EventSort;
import ru.practicum.event.util.EventState;
import ru.practicum.event.util.EventStateAction;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.location.mapper.LocationMapper;
import ru.practicum.location.model.Location;
import ru.practicum.location.repo.LocationRepository;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.mapper.ParticipationRequestMapper;
import ru.practicum.participationRequest.model.ParticipationRequest;
import ru.practicum.participationRequest.repo.ParticipationRequestRepository;
import ru.practicum.participationRequest.util.ParticipationRequestState;
import ru.practicum.user.model.User;
import ru.practicum.user.repo.UserRepository;
import ru.practicum.util.Constants;
import ru.practicum.util.exeption.CustomException;
import ru.practicum.util.exeption.NotFoundException;
import ru.practicum.util.exeption.RequestException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    public static final int ONE_HOURS = 1;
    public static final int TWO_HOURS = 2;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    @Value("${STAT_SERVER_URL:http://localhost:9090}")
    private String statClientUrl;

    private StatisticsClient client;

    @PostConstruct
    private void init() {
        client = new StatisticsClient(statClientUrl);
    }

    @Override
    @Transactional
    public List<EventFullDto> getAllByAdmin(List<Long> users, List<EventState> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {

        Pageable pageable = PageRequest.of(from, size);
        if (rangeStart == null) rangeStart = LocalDateTime.now();
        if (rangeEnd == null) rangeEnd = Constants.getMaxDateTime();

        Page<Event> page = eventRepository.findAllByAdmin(users, states, categories, rangeStart, rangeEnd, pageable);

        List<String> eventUrls = page.getContent().stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());

        List<ViewEndpointDto> viewStatsListDto = client.findStats(rangeStart.format(Constants.getDefaultDateTimeFormatter()),
                rangeEnd.format(Constants.getDefaultDateTimeFormatter()), eventUrls, true);

        return page.getContent().stream()
                .map(EventMapper::toFullDto)
                .peek(dto -> {
                    Optional<ViewEndpointDto> matchingStats = viewStatsListDto.stream()
                            .filter(statsDto -> statsDto.getUri().equals("/events/" + dto.getId()))
                            .findFirst();
                    dto.setViews(matchingStats.map(ViewEndpointDto::getHits).orElse(0L));
                })
                .peek(dto -> dto.setConfirmedRequests(participationRequestRepository.countByEventIdAndStatus(dto.getId(), ParticipationRequestState.CONFIRMED)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<EventShortDto> getAllPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable, EventSort sort, int from, int size, HttpServletRequest request) {

        if (categories != null && categories.size() == 1 && categories.get(0).equals(0L)) categories = null;
        if (rangeStart == null) rangeStart = LocalDateTime.now();
        if (rangeEnd == null) rangeEnd = Constants.getMaxDateTime();

        List<Event> eventList = eventRepository.getAllPublic(text, categories, paid, rangeStart, rangeEnd);

        if (onlyAvailable) {
            eventList = eventList.stream()
                    .filter(
                            event -> event.getParticipantLimit().equals(0)
                            || event.getParticipantLimit() < participationRequestRepository.countByEventIdAndStatus(
                                    event.getId(), ParticipationRequestState.CONFIRMED
                            ))
                    .collect(Collectors.toList());
        }

        List<String> eventUrls = eventList.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());

        List<ViewEndpointDto> viewStatsListDto = client.findStats(
                rangeStart.format(Constants.getDefaultDateTimeFormatter()),
                rangeEnd.format(Constants.getDefaultDateTimeFormatter()),
                eventUrls, true
        );

        List<EventShortDto> eventShortDtoList = eventList.stream()
                .map(EventMapper::toShortDto)
                .peek(dto -> {
                    Optional<ViewEndpointDto> matchingStats = viewStatsListDto.stream()
                            .filter(statsDto -> statsDto.getUri().equals("/events/" + dto.getId()))
                            .findFirst();
                    dto.setViews(matchingStats.map(ViewEndpointDto::getHits).orElse(0L));
                })
                .peek(dto -> dto.setConfirmedRequests(participationRequestRepository.countByEventIdAndStatus(dto.getId(), ParticipationRequestState.CONFIRMED)))
                .collect(Collectors.toList());

        switch (sort) {
            case EVENT_DATE:
                eventShortDtoList.sort(Comparator.comparing(EventShortDto::getEventDate));
                break;
            case VIEWS:
                eventShortDtoList.sort(Comparator.comparing(EventShortDto::getViews).reversed());
                break;
        }

        if (from >= eventShortDtoList.size()) return Collections.emptyList();
        int toIndex = Math.min(from + size, eventShortDtoList.size());
        return eventShortDtoList.subList(from, toIndex);
    }

    @Override
    @Transactional
    public EventFullDto patchByAdmin(long eventId, EventRequestUpdateDto eventRequestUpdateDto) {
        Event event = findEventById(eventId);

        if (eventRequestUpdateDto.getEventTimestamp() != null && LocalDateTime.now().plusHours(ONE_HOURS).isAfter(eventRequestUpdateDto.getEventTimestamp())) {
            throw new CustomException("Дата события должна быть через 1 час после текущего времени или позже!");
        }

        if (eventRequestUpdateDto.getStateAction() != null) {
            if (eventRequestUpdateDto.getStateAction().equals(EventStateAction.PUBLISH_EVENT.toString()) &&
                    !event.getState().equals(EventState.PENDING)) {
                throw new CustomException("Публикация события не возможна, не верное состояние: " + event.getState());
            }

            if (eventRequestUpdateDto.getStateAction().equals(EventStateAction.REJECT_EVENT.toString()) &&
                    event.getState().equals(EventState.PUBLISHED)) {
                throw new CustomException("Отклонение события не возможна, не верное состояние: " + event.getState());
            }
        }

        if (eventRequestUpdateDto.getCategory() != null) {
            event.setCategory(findCategoryById(eventRequestUpdateDto.getCategory()));
        }

        if (eventRequestUpdateDto.getLocation() != null) {
            event.setLocation(handleLocationDto(eventRequestUpdateDto.getLocation()));
        }

        Optional.ofNullable(eventRequestUpdateDto.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(eventRequestUpdateDto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(eventRequestUpdateDto.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(eventRequestUpdateDto.getEventTimestamp()).ifPresent(event::setEventDate);
        Optional.ofNullable(eventRequestUpdateDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(eventRequestUpdateDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(eventRequestUpdateDto.getRequestModeration()).ifPresent(event::setRequestModeration);

        if (eventRequestUpdateDto.getStateAction() != null) {
            switch (EventStateAction.valueOf(eventRequestUpdateDto.getStateAction())) {
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }

        event = eventRepository.save(event);
        return EventMapper.toFullDto(event);
    }

    @Override
    public List<EventShortDto> getAllByInitiator(long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<Event> page = eventRepository.findAllByInitiatorId(userId, pageable);
        if (page.isEmpty()) return new ArrayList<>();
        return page.getContent().stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getByIdByInitiator(long userId, long eventId) {
        Event event = findEventById(eventId);
        checkInitiator(userId, eventId, event.getInitiator().getId());
        return EventMapper.toFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsByInitiator(long userId, long eventId) {
        findUserById(userId);
        findEventById(eventId);
        return participationRequestRepository.findAllByEventId(eventId).stream()
                .map(ParticipationRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto create(long userId, EventNewDto eventNewDto) {

        if (eventNewDto.getEventTimestamp().isBefore(LocalDateTime.now().plusHours(TWO_HOURS))) {
            throw new RequestException("Дата события должна быть через 2 часа после текущего времени или позже.");
        }

        User user = findUserById(userId);
        Category category = findCategoryById(eventNewDto.getCategory());
        Location location = handleLocationDto(eventNewDto.getLocation());

        Event event = EventMapper.fromDto(eventNewDto, category, location);

        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);

        if (eventNewDto.getPaid() == null) event.setPaid(false);
        if (eventNewDto.getParticipantLimit() == null) event.setParticipantLimit(0);
        if (eventNewDto.getRequestModeration() == null) event.setRequestModeration(true);
        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto patchByInitiator(long userId, long eventId, EventRequestUpdateDto eventRequestUpdateDto) {

        Event event = findEventById(eventId);
        checkInitiator(userId, eventId, event.getInitiator().getId());

        if (eventRequestUpdateDto.getEventTimestamp() != null && LocalDateTime.now().plusHours(2).isAfter(eventRequestUpdateDto.getEventTimestamp())) {
            throw new CustomException("Дата события должна быть через 2 часа после текущего времени или позже.");
        }

        if (!(event.getState().equals(EventState.CANCELED) || event.getState().equals(EventState.PENDING))) {
            throw new CustomException("Можно изменить только отложенные или отмененные события.");
        }

        if (eventRequestUpdateDto.getCategory() != null) event.setCategory(findCategoryById(eventRequestUpdateDto.getCategory()));
        if (eventRequestUpdateDto.getLocation() != null) event.setLocation(handleLocationDto(eventRequestUpdateDto.getLocation()));

        Optional.ofNullable(eventRequestUpdateDto.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(eventRequestUpdateDto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(eventRequestUpdateDto.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(eventRequestUpdateDto.getEventTimestamp()).ifPresent(event::setEventDate);
        Optional.ofNullable(eventRequestUpdateDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(eventRequestUpdateDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(eventRequestUpdateDto.getRequestModeration()).ifPresent(event::setRequestModeration);

        if (eventRequestUpdateDto.getStateAction() != null) {

            EventStateAction state = EventStateAction.valueOf(eventRequestUpdateDto.getStateAction());

            switch (state) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }

        event = eventRepository.save(event);
        return EventMapper.toFullDto(event);
    }

    @Override
    @Transactional
    public EventResultUpdateStatusDto patchParticipationRequestsByInitiator(long userId, long eventId, EventRequestUpdateStatusDto eventRequestUpdateStatusDto) {

        findUserById(userId);
        Event event = findEventById(eventId);

        long confirmLimit = event.getParticipantLimit() - participationRequestRepository.countByEventIdAndStatus(eventId, ParticipationRequestState.CONFIRMED);

        if (confirmLimit <= 0) throw new CustomException("Лимит участников достигнут.");

        List<ParticipationRequest> requestList = participationRequestRepository.findAllByIdIn(eventRequestUpdateStatusDto.getRequestIds());

        List<Long> notFoundIds = eventRequestUpdateStatusDto.getRequestIds().stream()
                .filter(requestId -> requestList.stream().noneMatch(request -> request.getId().equals(requestId)))
                .collect(Collectors.toList());

        if (!notFoundIds.isEmpty()) {
            throw new NotFoundException("Заявка на участие с идентификатором " + notFoundIds + " не найдена!");
        }

        EventResultUpdateStatusDto eventResultUpdateStatusDto = EventResultUpdateStatusDto.builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>())
                .build();

        for (ParticipationRequest req : requestList) {

            if (!req.getEvent().getId().equals(eventId)) {
                throw new NotFoundException("Заявка на участие с идентификатором " + notFoundIds + " не найдена!");
            }

            if (confirmLimit <= 0) {
                req.setStatus(ParticipationRequestState.REJECTED);
                eventResultUpdateStatusDto.getRejectedRequests().add(ParticipationRequestMapper.toDto(req));
                continue;
            }

            switch (eventRequestUpdateStatusDto.getStatus()) {
                case CONFIRMED:
                    req.setStatus(ParticipationRequestState.CONFIRMED);
                    eventResultUpdateStatusDto.getConfirmedRequests().add(ParticipationRequestMapper.toDto(req));
                    confirmLimit--;
                    break;
                case REJECTED:
                    req.setStatus(ParticipationRequestState.REJECTED);
                    eventResultUpdateStatusDto.getRejectedRequests().add(ParticipationRequestMapper.toDto(req));
                    break;
            }
        }

        participationRequestRepository.saveAll(requestList);
        return eventResultUpdateStatusDto;
    }

    @Override
    public EventFullDto getByIdPublic(long eventId, HttpServletRequest request) {

        Event event = findEventById(eventId);

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Событие с идентификатором " + eventId + " не найдено!");
        }

        List<String> eventUrls = Collections.singletonList("/events/" + event.getId());

        List<ViewEndpointDto> viewEndpointProjectionList = client.findStats(Constants.getMinDateTime().format(Constants.getDefaultDateTimeFormatter()),
                Constants.getMaxDateTime().plusYears(1).format(Constants.getDefaultDateTimeFormatter()), eventUrls, true);

        EventFullDto dto = EventMapper.toFullDto(event);
        dto.setViews(viewEndpointProjectionList.isEmpty() ? 0L : viewEndpointProjectionList.get(0).getHits());
        dto.setConfirmedRequests(participationRequestRepository.countByEventIdAndStatus(dto.getId(), ParticipationRequestState.CONFIRMED));
        return dto;
    }

    private Event findEventById(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие с идентификатором " + id + " не найдено!"));
    }

    private User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + id + " не найден!"));
    }

    private Category findCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с идентификатором " + id + " не найдена!"));
    }

    private void checkInitiator(long userId, long eventId, long initiatorId) {
        if (userId != initiatorId) {
            throw new NotFoundException("Событие с идентификатором " + eventId + " не найдено!");
        }
    }

    private Location handleLocationDto(LocationDto locationDto) {
        Location location = locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon());
        return location != null ? location : locationRepository.save(LocationMapper.fromDto(locationDto));
    }
}