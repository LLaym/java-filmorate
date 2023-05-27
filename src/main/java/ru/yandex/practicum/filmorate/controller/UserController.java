package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.Validator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FilmService filmService;

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

    @DeleteMapping("{id}")
    public boolean deleteUserById(@PathVariable Integer id) {
        Validator.validateUserId(id);
        return userService.deleteUserById(id);
    }

    @PutMapping("{id}/friends/{friendId}")
    public void makeTwoUsersFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        Validator.validateUserId(id);
        Validator.validateUserId(friendId);

        userService.makeFriendship(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    public boolean makeTwoUsersStopBeingFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        Validator.validateUserId(id);
        Validator.validateUserId(friendId);

        return userService.dropFriendship(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> findUserFriends(@PathVariable Integer id) {
        Validator.validateUserId(id);

        return userService.findUserFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> findUsersMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        Validator.validateUserId(id);
        Validator.validateUserId(otherId);

        return userService.findUsersMutualFriends(id, otherId);
    }

    @GetMapping("{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable Integer id) {
        Validator.validateUserId(id);

        return filmService.getRecommendations(id);
    }

    @GetMapping("{id}/feed")
    public List<Event> getFeed(@PathVariable Integer id) {
        Validator.validateUserId(id);

        return userService.getFeed(id);
    }
}