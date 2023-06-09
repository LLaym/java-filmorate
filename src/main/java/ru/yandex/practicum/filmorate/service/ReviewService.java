package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.model.EventOperation.*;
import static ru.yandex.practicum.filmorate.model.EventType.REVIEW;

@Slf4j
@Service
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final ReviewRatingStorage reviewRatingStorage;
    private final EventStorage eventStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public ReviewService(ReviewStorage reviewStorage,
                         ReviewRatingStorage reviewRatingStorage,
                         EventStorage eventStorage,
                         FilmStorage filmStorage,
                         UserStorage userStorage) {
        this.reviewStorage = reviewStorage;
        this.reviewRatingStorage = reviewRatingStorage;
        this.eventStorage = eventStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Review createReview(Review review) {
        Integer filmId = review.getFilmId();
        Integer userId = review.getUserId();
        if (filmStorage.notExists(filmId)) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        } else if (userStorage.notExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        int generatedId = reviewStorage.save(review);
        Review createdReview = reviewStorage.findById(generatedId).orElse(null);

        log.info("Добавлен отзыв: {}", createdReview);

        Event event = Event.builder()
                .userId(userId)
                .entityId(generatedId)
                .eventType(REVIEW)
                .operation(ADD)
                .build();
        eventStorage.save(event);

        return createdReview;
    }

    public Review updateReview(Review review) {
        Integer reviewId = review.getReviewId();
        if (reviewStorage.notExists(reviewId)) {
            throw new NotFoundException("Отзыв с id " + reviewId + " не найден");
        }

        reviewStorage.update(review);

        Review updatedReview = reviewStorage.findById(review.getReviewId()).orElse(null);

        log.info("Обновлён отзыв: {}", updatedReview);

        Integer reviewerId = Objects.requireNonNull(updatedReview).getUserId();
        Event event = Event.builder()
                .userId(reviewerId)
                .entityId(reviewId)
                .eventType(REVIEW)
                .operation(UPDATE)
                .build();
        eventStorage.save(event);

        return updatedReview;
    }

    public Review getReviewById(Integer reviewId) {
        Review review = reviewStorage.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Отзыв с id " + reviewId + " не найден"));

        log.info("Получен отзыв: {}", review);
        return review;
    }

    public List<Review> getAllReviews(Integer limit) {
        List<Review> reviews = reviewStorage.findAll(limit);

        log.info("Возвращен список всех отзывов");
        return reviews;
    }

    public List<Review> getAllReviewsByFilmId(Integer filmId, Integer limit) {
        if (filmStorage.notExists(filmId)) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }

        log.info("Возвращен список всех отзывов фильма с id {}", filmId);
        return reviewStorage.findAllByFilmId(filmId, limit);
    }

    public boolean deleteReview(Integer reviewId) {
        if (reviewStorage.notExists(reviewId)) {
            throw new NotFoundException("Отзыв с id " + reviewId + " не найден");
        }

        Integer reviewerId = reviewStorage.findReviewer(reviewId);
        if (reviewStorage.delete(reviewId)) {
            log.info("Удален отзыв с id {}", reviewId);

            Event event = Event.builder()
                    .userId(reviewerId)
                    .entityId(reviewId)
                    .eventType(REVIEW)
                    .operation(REMOVE)
                    .build();
            eventStorage.save(event);

            return true;
        }
        return false;
    }

    public void likeReview(Integer reviewId, Integer userId) {
        if (reviewStorage.notExists(reviewId)) {
            throw new NotFoundException("Отзыв с id " + reviewId + " не найден");
        } else if (userStorage.notExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        reviewRatingStorage.save(reviewId, userId, true);
        log.info("Пользователь с id {} поставил лайк отзыву с id {}", userId, reviewId);
    }

    public void dislikeReview(Integer reviewId, Integer userId) {
        if (reviewStorage.notExists(reviewId)) {
            throw new NotFoundException("Отзыв с id " + reviewId + " не найден");
        } else if (userStorage.notExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        reviewRatingStorage.save(reviewId, userId, false);
        log.info("Пользователь с id {} поставил дизлайк отзыву с id {}", userId, reviewId);
    }

    public void deleteLike(Integer reviewId, Integer userId) {
        if (reviewStorage.notExists(reviewId)) {
            throw new NotFoundException("Отзыв с id " + reviewId + " не найден");
        } else if (userStorage.notExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        reviewRatingStorage.delete(reviewId, userId, true);
        log.info("Пользователь с id {} убрал лайк с отзыва с id {}", userId, reviewId);
    }

    public void deleteDislike(Integer reviewId, Integer userId) {
        if (reviewStorage.notExists(reviewId)) {
            throw new NotFoundException("Отзыв с id " + reviewId + " не найден");
        } else if (userStorage.notExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }

        reviewRatingStorage.delete(reviewId, userId, false);
        log.info("Пользователь с id {} убрал дизлайк с отзыва с id {}", userId, reviewId);
    }
}
