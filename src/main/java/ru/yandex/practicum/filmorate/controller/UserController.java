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
        Validator.validateUser(user);

        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        Validator.validateUser(user);

        return userService.updateUser(user);
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("{id}")
    public User findUserById(@PathVariable Integer id) {
        Validator.validateUserId(id);

        return userService.findUserById(id);
    }

    @PutMapping("{id}/friends/{friendId}")
    public Collection<User> makeTwoUsersFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        Validator.validateUserId(id);
        Validator.validateUserId(friendId);

        return userService.makeFriendship(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public Collection<User> makeTwoUsersStopBeingFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        Validator.validateUserId(id);
        Validator.validateUserId(friendId);

        return userService.dropFriendship(id, friendId);
    }

    @GetMapping("{id}/friends")
    public Collection<User> findUserFriends(@PathVariable Integer id) {
        Validator.validateUserId(id);

        return userService.findUserFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public Collection<User> findUsersMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        Validator.validateUserId(id);
        Validator.validateUserId(otherId);

        return userService.findUsersMutualFriends(id, otherId);
    }
}