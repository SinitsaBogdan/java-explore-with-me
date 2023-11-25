package ru.practicum.util.exeptions;

import lombok.Getter;

@Getter
public enum ExceptionMessages {

    VALIDATOR_ERROR__NOT_VALID_DATETIME("VALIDATE", "Даты для поиска установлены неправильно или не корректны."),
    VALIDATOR_ERROR__START_DATETIME__IS_NULL("VALIDATE", "Требуемый параметр запроса 'start' для параметра метода типа LocalDateTime отсутствует."),
    VALIDATOR_ERROR__END_DATETIME__IS_NULL("VALIDATE", "Требуемый параметр запроса 'end' для параметра метода типа LocalDateTime отсутствует.");

    private final String category;
    private final String description;

    ExceptionMessages(String category, String description) {
        this.category = category;
        this.description = description;
    }
}