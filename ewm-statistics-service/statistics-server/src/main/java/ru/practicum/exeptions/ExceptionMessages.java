package ru.practicum.exeptions;

import lombok.Getter;

@Getter
public enum ExceptionMessages {

    VALIDATOR_ERROR__NOT_VALID_DATETIME("VALIDATE", "Даты для поиска установлены неправильно или не корректны.");

    private final String category;
    private final String description;

    ExceptionMessages(String category, String description) {
        this.category = category;
        this.description = description;
    }
}
