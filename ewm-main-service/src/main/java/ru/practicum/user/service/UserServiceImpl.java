package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repo.UserRepository;
import ru.practicum.util.exeption.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> get(List<Long> ids, int from, int size) {

        Pageable pageable = PageRequest.of(from, size);

        if (ids != null && !ids.isEmpty()) {
            return userRepository.findAllByIdIn(ids, pageable)
                    .getContent().stream()
                    .map(UserMapper::toDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(pageable)
                    .getContent().stream()
                    .map(UserMapper::toDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.fromDto(userDto);
        User saveUser = userRepository.save(user);
        return UserMapper.toDto(saveUser);
    }

    @Override
    public void delete(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + "не найден!"));

        userRepository.deleteById(userId);
    }
}