package ru.practicum.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class RequestCommentDto {

    @NotNull
    @NotBlank
    @Size(max = 5000)
    private String text;
}