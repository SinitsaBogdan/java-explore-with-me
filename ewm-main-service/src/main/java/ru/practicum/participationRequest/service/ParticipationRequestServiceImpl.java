package ru.practicum.participationRequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.repo.EventRepository;
import ru.practicum.event.util.EventState;
import ru.practicum.participationRequest.dto.ParticipationRequestDto;
import ru.practicum.participationRequest.mapper.ParticipationRequestMapper;
import ru.practicum.participationRequest.model.ParticipationRequest;
import ru.practicum.participationRequest.repo.ParticipationRequestRepository;
import ru.practicum.participationRequest.util.ParticipationRequestState;
import ru.practicum.user.model.User;
import ru.practicum.user.repo.UserRepository;
import ru.practicum.util.exeption.CustomException;
import ru.practicum.util.exeption.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository participationRequestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getAll(long userId) {
        findUserById(userId);
        return participationRequestRepository.findAllByRequesterId(userId).stream()
                .map(ParticipationRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto create(long userId, long eventId) {

        User requester = findUserById(userId);
        Event event = findEventById(eventId);

        if (event.getInitiator().getId().equals(userId)) {
            throw new CustomException("Создатель мероприятия не может отправить заявку на участие в собственном мероприятии!");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new CustomException("Невозможно принять участие в неопубликованном мероприятии!");
        }

        if (event.getParticipantLimit() > 0) {
            if (event.getParticipantLimit() <= participationRequestRepository.countByEventIdAndStatus(eventId, ParticipationRequestState.CONFIRMED)) {
                throw new CustomException("Лимит заявок на участие для данного мероприятия исчерпан!");
            }
        }

        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setRequester(requester);
        participationRequest.setEvent(event);
        participationRequest.setCreated(LocalDateTime.now());
        participationRequest.setStatus(event.getRequestModeration() && !event.getParticipantLimit().equals(0) ? ParticipationRequestState.PENDING : ParticipationRequestState.CONFIRMED);

        return ParticipationRequestMapper.toDto(participationRequestRepository.save(participationRequest));
    }

    @Override
    @Transactional
    public ParticipationRequestDto patch(long userId, long requestId) {
        findUserById(userId);
        ParticipationRequest participationRequest = findParticipationRequestById(requestId);

        if (!participationRequest.getRequester().getId().equals(userId)) {
            throw new NotFoundException("Не найдено событий, доступных для редактирования!");
        }

        participationRequest.setStatus(ParticipationRequestState.CANCELED);
        return ParticipationRequestMapper.toDto(participationRequestRepository.save(participationRequest));
    }

    private ParticipationRequest findParticipationRequestById(long id) {
        return participationRequestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заявка на участие с id " + id + " не найдена!"));
    }

    private User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден!"));
    }

    private Event findEventById(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие с id " + id + " не найдено!"));
    }
}