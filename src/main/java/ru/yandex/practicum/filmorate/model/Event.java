package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class Event {
    private Integer eventId;
    @NotNull
    private Integer userId;
    @NotNull
    private Integer entityId;
    private EventType eventType;
    private EventOperation operation;
    @NotNull
    private Long timestamp;
}
