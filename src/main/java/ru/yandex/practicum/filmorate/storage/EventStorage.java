package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventStorage {
    int save(Event event);

    List<Event> findAllByUserId(int userId);
}
