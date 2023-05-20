package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.validator.Validator;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @PostMapping
    public Director createDirector(@RequestBody @Valid Director director) {
        return directorService.createDirector(director);
    }

    @PutMapping
    public Director updateDirector(@RequestBody Director director) {
        Validator.validateDirector(director);

        return directorService.updateDirector(director);
    }

    @GetMapping
    public Collection<Director> findAllDirectors() {
        return directorService.findAllDirectors();
    }

    @GetMapping("{id}")
    public Director findDirectorById(@PathVariable Integer id) {
        Validator.validateDirectorId(id);

        return directorService.findDirectorById(id);
    }

    @DeleteMapping("{id}")
    public boolean deleteDirector(@PathVariable Integer id) {
        Validator.validateDirectorId(id);

        return directorService.deleteDirector(id);
    }
}
