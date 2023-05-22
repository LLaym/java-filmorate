package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
public class Review {
    private Integer reviewId;
    @NotNull
    private Integer filmId;
    @NotNull
    private Integer userId;
    @Size(max = 500)
    @NotNull
    private String content;
    @NotNull
    private Boolean isPositive;
    @NotNull
    private int useful;

    // Метод нужен для прохождения тестов (Lombok убирает is у переменной isPositive)
    public Boolean getIsPositive() {
        return isPositive;
    }
}
