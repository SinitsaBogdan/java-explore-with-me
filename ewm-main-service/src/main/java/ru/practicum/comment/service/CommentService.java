package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.RequestCommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getAllByEventId(long eventId);

    CommentDto add(long userId, long eventId, RequestCommentDto dto);

    CommentDto updateByUser(long userId, long commentId, RequestCommentDto updateRequest);

    CommentDto updateByAdmin(long commentId, RequestCommentDto updateRequest);

    void deleteByUser(long userId, long commentId);

    void deleteByAdmin(long commentId);
}