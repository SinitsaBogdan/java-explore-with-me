package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.RequestCommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repo.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repo.EventRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repo.UserRepository;
import ru.practicum.util.exeption.AccessDeniedException;
import ru.practicum.util.exeption.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CommentDto> getAllByEventId(long eventId) {
        return commentRepository.findAllByEventId(eventId).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto add(long userId, long eventId, RequestCommentDto dto) {

        User user = findUserById(userId);
        Event event = findEventById(eventId);
        Comment comment = new Comment();

        comment.setUser(user);
        comment.setEvent(event);
        comment.setText(dto.getText());

        LocalDateTime time = LocalDateTime.now();
        comment.setCreated(time);
        comment.setUpdated(time);

        comment = commentRepository.save(comment);
        return CommentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public CommentDto updateByUser(long userId, long commentId, RequestCommentDto updateRequest) {
        findUserById(userId);
        Comment comment = findCommentById(commentId);

        if (!comment.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Пользователь с id " + userId + " не является владельцем комментария с id " + commentId);
        }

        Optional.ofNullable(updateRequest.getText()).ifPresent(comment::setText);
        comment.setUpdated(LocalDateTime.now());
        return CommentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public CommentDto updateByAdmin(long commentId, RequestCommentDto updateRequest) {

        Comment comment = findCommentById(commentId);
        Optional.ofNullable(updateRequest.getText()).ifPresent(comment::setText);
        comment.setUpdated(LocalDateTime.now());
        return CommentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public void deleteByUser(long userId, long commentId) {

        findUserById(userId);
        Comment comment = findCommentById(commentId);

        if (!comment.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Пользователь с id " + userId + " не является владельцем комментария с id " + commentId);
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void deleteByAdmin(long commentId) {
        Comment comment = findCommentById(commentId);
        commentRepository.deleteById(comment.getId());
    }

    private Comment findCommentById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий с id " + id + " не найден"));
    }

    private User findUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
    }

    private Event findEventById(long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие с id " + id + " не найдено"));
    }
}