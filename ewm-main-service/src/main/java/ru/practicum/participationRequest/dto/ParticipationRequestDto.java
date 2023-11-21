package ru.practicum.participationRequest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.practicum.participationRequest.util.ParticipationRequestState;
import ru.practicum.util.Constants;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {

    private Long id;

    private Long requester;

    private Long event;

    private ParticipationRequestState status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATE_TEMPLATE)
    private LocalDateTime created;
}