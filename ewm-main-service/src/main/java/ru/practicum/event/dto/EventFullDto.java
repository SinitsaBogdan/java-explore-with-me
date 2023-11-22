package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.util.EventState;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.util.Constants;

import java.time.LocalDateTime;

@Data
@Builder
@Validated
public class EventFullDto {

    private Long id;

    private UserShortDto initiator;
    private CategoryDto category;
    private LocationDto location;

    private String title;
    private String annotation;
    private String description;
    private EventState state;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TEMPLATE)
    private LocalDateTime eventDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TEMPLATE)
    private LocalDateTime createdOn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TEMPLATE)
    private LocalDateTime publishedOn;

    private Integer participantLimit;
    private Boolean paid;
    private Boolean requestModeration;

    private Long confirmedRequests;
    private Long views;
}