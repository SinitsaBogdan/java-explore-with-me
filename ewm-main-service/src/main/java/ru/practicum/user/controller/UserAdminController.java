package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserAdminController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> get(
            @RequestParam(required = false) List<Long> ids,
            @Valid @RequestParam(defaultValue = "0") @Min(0) int from,
            @Valid @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        log.info("\nGET [http://localhost:8080/admin/users] : запрос на просмотр пользователей\n");
        return userService.get(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        log.info("\nPOST [http://localhost:8080/admin/users] : запрос на создание пользователя {}\n", userDto);
        return userService.create(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long userId) {
        log.info("\nDELETE [http://localhost:8080/admin/users] : запрос на удаление пользователя {}\n", userId);
        userService.delete(userId);
    }
}