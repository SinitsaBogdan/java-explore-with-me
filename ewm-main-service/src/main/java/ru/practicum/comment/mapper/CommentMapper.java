package ru.practicum.comment.mapper;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.util.exeption.CustomException;

public final class CommentMapper {

    private CommentMapper() {
        throw new CustomException("Это служебный класс, и его экземпляр не может быть создан");
    }

    public static CommentDto toDto(Comment model) {
        return CommentDto.builder()
                .userName(model.getUser().getName())
                .created(model.getCreated())
                .updated(model.getUpdated())
                .text(model.getText())
                .id(model.getId())
                .build();
    }

    public static Comment fromDto(CommentDto dto) {
        return Comment.builder()
                .id(dto.getId())
                .text(dto.getText())
                .created(dto.getCreated())
                .build();
    }
}