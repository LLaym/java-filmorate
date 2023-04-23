//package ru.yandex.practicum.filmorate.storage.impl;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.storage.UserStorage;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Qualifier("inMemoryUserStorage")
//public class InMemoryUserStorage implements UserStorage {
//    private final Map<Integer, User> users = new HashMap<>();
//
//    @Override
//    public User saveUser(User user) {
//        users.put(user.getId(), user);
//        return user;
//    }
//
//    @Override
//    public User updateUser(User user) {
//        users.put(user.getId(), user);
//        return user;
//    }
//
//    @Override
//    public Collection<User> getAllUsers() {
//        return users.values();
//    }
//
//    @Override
//    public User getUserById(Integer id) {
//        return users.get(id);
//    }
//
//    @Override
//    public Collection<User> saveFriendship(Integer id, Integer friendId) {
//        // Метод затычка
//        return null;
//    }
//
//    @Override
//    public Collection<User> removeFriendship(Integer id, Integer friendId) {
//        // Метод затычка
//        return null;
//    }
//}