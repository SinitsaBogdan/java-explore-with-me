package ru.practicum.user.mapper;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;
import ru.practicum.util.exeption.CustomException;

public final class UserMapper {

    private UserMapper() {
        throw new CustomException("Это служебный класс, и его экземпляр не может быть создан");
    }

    public static UserDto toDto(User model) {
        return UserDto.builder()
                .email(model.getEmail())
                .id(model.getId())
                .name(model.getName())
                .build();
    }

    public static UserShortDto toShortDto(User model) {
        return UserShortDto.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
    }

    public static User fromDto(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }
}