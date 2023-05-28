package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> findById(int mpaId) {
        String findByIdQuery = "SELECT * FROM mpas WHERE id = ?";

        return jdbcTemplate.query(findByIdQuery, ((rs, rowNum) -> makeMpa(rs)), mpaId)
                .stream().findFirst();
    }

    @Override
    public List<Mpa> findAll() {
        String findAllQuery = "SELECT * FROM mpas ORDER BY id";

        return jdbcTemplate.query(findAllQuery, ((rs, rowNum) -> makeMpa(rs)));
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("id"), rs.getString("name"));
    }
}
