package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class MpaRatingDbStorage implements MpaRatingStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaRatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MpaRating getMpaRatingById(Integer id) {
        String sql = "SELECT * FROM mpa_ratings WHERE id = ?";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeMpaRating(rs)), id)
                .stream().findFirst().get();
    }

    @Override
    public Collection<MpaRating> getAllMpaRatings() {
        String sql = "SELECT * FROM mpa_ratings";

        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeMpaRating(rs)));
    }

    private MpaRating makeMpaRating(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");

        return new MpaRating(id, name);
    }
}
