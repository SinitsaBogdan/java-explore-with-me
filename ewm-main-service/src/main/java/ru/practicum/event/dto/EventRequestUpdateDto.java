package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.util.EventStateAction;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.util.Constants;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestUpdateDto {

    private Long category;
    private LocationDto location;

    @Size(min = 3, max = 200)
    private String title;

    @Size(min = 20, max = 3000)
    private String annotation;

    @Size(min = 20, max = 10000)
    private String description;

    @Future
    @JsonProperty("eventDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TEMPLATE)
    private LocalDateTime eventTimestamp;

    private Integer participantLimit;
    private Boolean paid;
    private Boolean requestModeration;
    private EventStateAction eventStateAction;
}
