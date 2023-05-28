package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.ReviewRatingStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

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

    public ReviewService(ReviewStorage reviewStorage,
                         ReviewRatingStorage reviewRatingStorage,
                         EventStorage eventStorage) {
        this.reviewStorage = reviewStorage;
        this.reviewRatingStorage = reviewRatingStorage;
        this.eventStorage = eventStorage;
    }

    public Review createReview(Review review) {
        int generatedId = reviewStorage.save(review);
        Review createdReview = reviewStorage.findById(generatedId).orElse(null);

        log.info("Добавлен отзыв: {}", createdReview);

        int userId = Objects.requireNonNull(createdReview).getUserId();
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
        reviewStorage.update(review);
        Review updatedReview = reviewStorage.findById(review.getReviewId()).orElse(null);

        log.info("Обновлён отзыв: {}", updatedReview);

        int userId = Objects.requireNonNull(updatedReview).getUserId();
        int reviewId = Objects.requireNonNull(updatedReview).getReviewId();
        Event event = Event.builder()
                .userId(userId)
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

    public List<Review> getAllReviews(int limit) {
        log.info("Возвращен список всех отзывов");
        return reviewStorage.findAll(limit);
    }

    public List<Review> getAllReviewsByFilmId(int filmId, int limit) {
        log.info("Возвращен список всех отзывов фильма с id {}", filmId);
        return reviewStorage.findAllByFilmId(filmId, limit);
    }

    public boolean deleteReview(int reviewId) {
        int userId = reviewStorage.findUserId(reviewId);
        if (reviewStorage.delete(reviewId)) {
            log.info("Удален отзыв с id {}", reviewId);

            Event event = Event.builder()
                    .userId(userId)
                    .entityId(reviewId)
                    .eventType(REVIEW)
                    .operation(REMOVE)
                    .build();
            eventStorage.save(event);

            return true;
        }
        return false;
    }

    public boolean likeReview(Integer reviewId, Integer userId) {
        log.info("Пользователь с id {} поставил лайк отзыву с id {}", userId, reviewId);
        return reviewRatingStorage.save(reviewId, userId, true);
    }

    public boolean dislikeReview(Integer reviewId, Integer userId) {
        log.info("Пользователь с id {} поставил дизлайк отзыву с id {}", userId, reviewId);
        return reviewRatingStorage.save(reviewId, userId, false);
    }

    public boolean deleteLike(Integer reviewId, Integer userId) {
        log.info("Пользователь с id {} убрал лайк с отзыва с id {}", userId, reviewId);
        return reviewRatingStorage.delete(reviewId, userId, true);
    }

    public boolean deleteDislike(Integer reviewId, Integer userId) {
        log.info("Пользователь с id {} убрал дизлайк с отзыва с id {}", userId, reviewId);
        return reviewRatingStorage.delete(reviewId, userId, false);
    }

}
