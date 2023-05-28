package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.validator.Validator;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public Review createReview(@RequestBody @Valid Review review) {
        Validator.validateReview(review);

        return reviewService.createReview(review);
    }

    @PutMapping
    public Review updateReview(@RequestBody @Valid Review review) {
        Validator.validateReview(review);

        return reviewService.updateReview(review);
    }

    @DeleteMapping("{id}")
    public boolean deleteReviewById(@PathVariable Integer id) {
        Validator.validateReviewId(id);

        return reviewService.deleteReview(id);
    }

    @GetMapping("{id}")
    public Review findReviewById(@PathVariable Integer id) {
        Validator.validateReviewId(id);

        return reviewService.getReviewById(id);
    }

    @GetMapping
    public List<Review> findReviews(@RequestParam(required = false) Integer filmId,
                                    @RequestParam(defaultValue = "10") int count) {
        if (filmId == null) {
            return reviewService.getAllReviews(count);
        }

        Validator.validateFilmId(filmId);
        return reviewService.getAllReviewsByFilmId(filmId, count);
    }

    @PutMapping("{id}/like/{userId}")
    public void likeReview(@PathVariable int id, @PathVariable int userId) {
        Validator.validateReviewId(id);
        Validator.validateUserId(userId);

        reviewService.likeReview(id, userId);
    }

    @PutMapping("{id}/dislike/{userId}")
    public void dislikeReview(@PathVariable int id, @PathVariable int userId) {
        Validator.validateReviewId(id);
        Validator.validateUserId(userId);

        reviewService.dislikeReview(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        Validator.validateReviewId(id);
        Validator.validateUserId(userId);

        reviewService.deleteLike(id, userId);
    }

    @DeleteMapping("{id}/dislike/{userId}")
    public void deleteDislike(@PathVariable int id, @PathVariable int userId) {
        Validator.validateReviewId(id);
        Validator.validateUserId(userId);

        reviewService.deleteDislike(id, userId);
    }
}
