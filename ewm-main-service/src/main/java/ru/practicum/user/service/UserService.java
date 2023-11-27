package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAll(List<Long> ids, int from, int size);

    UserDto add(UserDto userDto);

    void delete(long userId);
}