package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FilmService filmService;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return userService.updateUser(user);
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable @NotNull Integer id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUserById(@PathVariable @NotNull Integer id) {
        return userService.deleteUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void makeTwoUsersFriends(@PathVariable @NotNull Integer id,
                                    @PathVariable @NotNull Integer friendId) {
        userService.makeFriendship(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void makeTwoUsersStopBeingFriends(@PathVariable @NotNull Integer id,
                                             @PathVariable @NotNull Integer friendId) {
        userService.dropFriendship(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findUserFriends(@PathVariable @NotNull Integer id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findUsersMutualFriends(@PathVariable @NotNull Integer id,
                                             @PathVariable @NotNull Integer otherId) {
        return userService.getUsersMutualFriends(id, otherId);
    }

    @GetMapping("/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable @NotNull Integer id) {
        return filmService.getRecommendations(id);
    }

    @GetMapping("/{id}/feed")
    public List<Event> getFeed(@PathVariable @NotNull Integer id) {
        return userService.getFeed(id);
    }
}