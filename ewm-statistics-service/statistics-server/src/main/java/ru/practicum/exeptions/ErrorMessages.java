package ru.practicum.exeptions;

import lombok.Getter;

@Getter
public enum ErrorMessages {

    VALIDATOR_ERROR__NOT_VALID_DATETIME("VALIDATE", "Даты для поиска установлены неправильно или не корректны.");

    private final String category;
    private final String description;

    ErrorMessages(String category, String description) {
        this.category = category;
        this.description = description;
    }
}
