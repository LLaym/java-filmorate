package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static int nextId;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        User userWithId = user.toBuilder().id(++nextId).build();
        users.put(userWithId.getId(), userWithId);
        return userWithId;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }
}