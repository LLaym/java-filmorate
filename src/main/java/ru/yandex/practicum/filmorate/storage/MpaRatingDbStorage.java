package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.util.Collection;

@Component
public class MpaRatingDbStorage implements MpaRatingStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MpaRating getMpaRatingById(Integer id) {
        return null;
    }

    @Override
    public Collection<MpaRating> getAllMpaRatings() {
        String sql = "SELECT * FROM mpa_ratings";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeMpaRating(rs)));
    }

    private MpaRating makeMpaRating(ResultSet rs) {
        return null;
    }
}
