package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Collection<User> findAllUsers() {
        return userStorage.getAllUsers();
    }

//    // TODO Метод может быть неверным
//    public String addToFriends(User user1, User user2) {
//        user1.getFriends().add(user2.getId());
//        user2.getFriends().add(user1.getId());
//        return String.format("%s и %s теперь друзья!", user1, user2);
//    }
//    // TODO Метод может быть неверным
//
//    public Collection<User> getMutualFriends(User user1, User user2) {
//        Set<Integer> user1Friends = user1.getFriends();
//        Set<Integer> user2Friends = user2.getFriends();
//        Set<Integer> common = new HashSet<>(user1Friends);
//        common.retainAll(user2Friends);
//
//        return userStorage.getAllUsers().stream()
//                .filter(user -> common.contains(user.getId())).collect(Collectors.toList());
//    }
}
