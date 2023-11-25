package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.practicum.event.util.EventStateAction;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestUpdateStatusDto {

    @NotNull
    private List<Long> requestIds;

    @NotNull
    private EventStateAction status;
}