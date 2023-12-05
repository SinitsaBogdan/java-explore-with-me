package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.RequestCommentDto;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class CommentPrivateController {

    private final CommentService commentService;

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(
            @PathVariable @Min(0) long userId,
            @PathVariable @Min(0) long eventId,
            @Valid @RequestBody RequestCommentDto dto
    ) {
        log.info("\nPOST [http://localhost:8080/users/{}/comments/{}] : запрос на добавление комментария владельцем к событию\n", userId, eventId);
        return commentService.add(userId, eventId, dto);
    }

    @PatchMapping("/{commentId}")
    public CommentDto patch(
            @PathVariable @Min(0) long userId,
            @PathVariable @Min(0) long commentId,
            @Valid @RequestBody RequestCommentDto updateComment
    ) {
        log.info("\nPATCH [http://localhost:8080/users/{}/comments/{}] : запрос на обновление комментария владельцем\n", userId, commentId);
        return commentService.updateByUser(userId, commentId, updateComment);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable @Min(0) long userId,
            @PathVariable @Min(0) long commentId
    ) {
        log.info("\nDELETE [http://localhost:8080/users/{}/comments/{}] : запрос на удаление комментария владельцем\n", userId, commentId);
        commentService.deleteByUser(userId, commentId);
    }
}