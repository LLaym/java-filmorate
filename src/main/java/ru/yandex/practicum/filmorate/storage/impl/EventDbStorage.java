package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;
    private final String getAllByUserId = "SELECT * FROM events WHERE user_id = ?";
    public EventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int save(Event event) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("events")
                .usingGeneratedKeyColumns("id");

        Integer userId = event.getUserId();
        Integer entityId = event.getEntityId();
        EventType eventType = event.getEventType();
        EventOperation operation = event.getOperation();

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("user_id", userId);
        parameters.put("entity_id", entityId);
        parameters.put("event_type", eventType);
        parameters.put("operation", operation);
        parameters.put("timestamp", Instant.now());

        return simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
    }

    @Override
    public List<Event> getAllByUserId(int userId) {
        return jdbcTemplate.query(getAllByUserId, (rs, rowNum) -> makeEvent(rs), userId);
    }

    private Event makeEvent(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        Integer userId = rs.getInt("user_id");
        Integer entityId = rs.getInt("entity_id");
        EventType eventType = EventType.valueOf(rs.getString("event_type"));
        EventOperation operation = EventOperation.valueOf(rs.getString("operation"));
        Long timestamp = rs.getTimestamp("timestamp").toInstant().toEpochMilli();

        return Event.builder()
                .eventId(id)
                .userId(userId)
                .entityId(entityId)
                .eventType(eventType)
                .operation(operation)
                .timestamp(timestamp)
                .build();
    }
}
