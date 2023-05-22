package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewRatingStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Slf4j
@Service
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final ReviewRatingStorage reviewRatingStorage;

    public ReviewService(ReviewStorage reviewStorage, ReviewRatingStorage reviewRatingStorage) {
        this.reviewStorage = reviewStorage;
        this.reviewRatingStorage = reviewRatingStorage;
    }

    public Review createReview(Review review) {
        int generatedId = reviewStorage.save(review);
        Review createdReview = reviewStorage.getById(generatedId).orElse(null);

        log.info("Добавлен отзыв: {}", createdReview);
        return createdReview;
    }

    public Review updateReview(Review review) {
        reviewStorage.update(review);
        Review updatedReview = reviewStorage.getById(review.getReviewId()).orElse(null);

        log.info("Обновлён отзыв: {}", updatedReview);
        return updatedReview;
    }

    public Review findReviewById(Integer reviewId) {
        Review review = reviewStorage.getById(reviewId)
                .orElseThrow(() -> new FilmNotFoundException("Отзыв с id " + reviewId + " не найден"));

        log.info("Получен отзыв: {}", review);
        return review;
    }

    public List<Review> findAllReviews(int limit) {
        log.info("Возвращен список всех отзывов");
        return reviewStorage.getAll(limit);
    }

    public List<Review> findAllReviewsByFilmId(int filmId, int limit) {
        log.info("Возвращен список всех отзывов фильма с id {}", filmId);
        return reviewStorage.getAllByFilmId(filmId, limit);
    }

    public boolean deleteReview(int reviewId) {
        log.info("Удален отзыв с id {}", reviewId);
        return reviewStorage.delete(reviewId);
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
