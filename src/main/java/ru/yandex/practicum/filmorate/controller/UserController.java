package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("{id}")
    public User findUserById(@PathVariable Integer id) {
        return userService.findUserById(id);
    }

    @PutMapping("{id}/friends/{friendId}")
    public Collection<User> makeTwoUsersFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        return userService.makeTwoUsersFriends(id, friendId);
    }
}
