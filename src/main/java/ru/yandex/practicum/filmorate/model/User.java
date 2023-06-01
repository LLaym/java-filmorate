package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {
    private Integer id;
    @Email
    @Size(max = 40)
    private String email;
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    @Size(max = 20)
    private String login;
    @Size(max = 100)
    private String name;
    @Past
    private LocalDate birthday;
    private Set<Integer> friends;
}