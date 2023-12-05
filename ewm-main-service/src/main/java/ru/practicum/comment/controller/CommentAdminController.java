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
@RequestMapping("/admin/comments")
public class CommentAdminController {

    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public CommentDto patch(
            @PathVariable @Min(0) long commentId,
            @Valid @RequestBody RequestCommentDto commentUpdateRequest) {
        log.info("\nPATCH [http://localhost:8080/admin/comments/{}] : запрос на обновление комментария администратором\n", commentId);
        return commentService.updateByAdmin(commentId, commentUpdateRequest);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Min(0) long commentId) {
        log.info("\nDELETE [http://localhost:8080/admin/comments/{}] : запрос на удаление комментария администратором\n", commentId);
        commentService.deleteByAdmin(commentId);
    }
}