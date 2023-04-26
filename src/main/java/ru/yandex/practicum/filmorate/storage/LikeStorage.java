package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {
    void save(Integer filmId, Integer userId);

    void delete(Integer filmId, Integer userId);
//    List<Integer> getTopFilmsId(Integer count);
}
