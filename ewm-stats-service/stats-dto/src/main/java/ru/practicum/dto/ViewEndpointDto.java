package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewEndpointDto {

    @NotBlank(message = "Параметр app не должен быть пустым")
    private String app;

    @NotBlank(message = "Параметр uri не должен быть пустым")
    private String uri;

    private long hits;
}