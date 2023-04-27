package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserDbStorage userStorage;

    @Test
    void testSave() {
        User user = User.builder().name("Vitaly").email("mail@yandex.ru")
                .login("LLaym").birthday(LocalDate.of(1995, Month.MAY, 11)).build();

        int generatedId = userStorage.save(user);

        assertEquals(1, generatedId);
    }

    @Test
    void testUpdate() {
        User updatedUser = User.builder().name("Updated Vitaly").email("mail@yandex.ru")
                .login("LLaym").birthday(LocalDate.of(1995, Month.MAY, 11)).build();

        boolean isUpdated = userStorage.update(updatedUser);

        assertTrue(isUpdated);
    }

    @Test
    void testGetById() {
        Optional<User> userOptional = userStorage.getById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void testGetAll() {
        assertEquals(1, userStorage.getAll().size());
    }
}