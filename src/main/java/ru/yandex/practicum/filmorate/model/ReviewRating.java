package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ReviewRating {
    private Integer reviewId;
    private Integer userId;
    @NotNull
    private boolean isLiked;
}
