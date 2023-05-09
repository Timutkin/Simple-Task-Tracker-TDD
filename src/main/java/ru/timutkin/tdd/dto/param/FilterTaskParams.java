package ru.timutkin.tdd.dto.param;

import lombok.AccessLevel;
import lombok.Builder;


import java.time.LocalDateTime;
import java.util.Optional;

@Builder
public record FilterTaskParams(Optional<LocalDateTime> after, Optional<LocalDateTime> before, Optional<String> taskName,
                               Optional<String> message, Optional<String> status, Optional<Long> userId,
                               Optional<Long> projectId) {
}
