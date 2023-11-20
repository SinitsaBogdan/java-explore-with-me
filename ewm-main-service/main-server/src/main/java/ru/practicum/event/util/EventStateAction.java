package ru.practicum.event.util;

public enum EventStateAction {

    CONFIRMED,
    REJECTED,

    SEND_TO_REVIEW,
    CANCEL_REVIEW,

    PUBLISH_EVENT,
    REJECT_EVENT
}