package ru.practicum.participationRequest.repo;

public interface ParticipationRequestCounts {

    void setEventId(Long eventId);

    Long getEventId();

    void setCount(Long count);

    Long getCount();
}