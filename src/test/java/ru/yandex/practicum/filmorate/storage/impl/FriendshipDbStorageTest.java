package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FriendshipDbStorageTest {
    private final FriendshipDbStorage friendshipStorage;
    private final UserDbStorage userStorage;

    @Test
    void test() {
        User user1 = User.builder()
                .name("Vitaly")
                .email("mail@yandex.ru")
                .login("LLaym")
                .birthday(LocalDate.of(1990, Month.OCTOBER, 25))
                .build();

        User user2 = User.builder()
                .name("VitalyFriend")
                .email("mail2@yandex.ru")
                .login("LLaymFriend")
                .birthday(LocalDate.of(1990, Month.OCTOBER, 26))
                .build();

        userStorage.save(user1);
        userStorage.save(user2);

        friendshipStorage.save(1, 2);

        List<Friendship> user1Friends = friendshipStorage.findAllByUserId(1);

        assertFalse(user1Friends.isEmpty());
        assertEquals(1, user1Friends.get(0).getId());
        assertEquals(2, user1Friends.get(0).getFriendId());

        friendshipStorage.delete(1, 2);

        List<Friendship> user1Friends2 = friendshipStorage.findAllByUserId(1);

        assertTrue(user1Friends2.isEmpty());
    }
}