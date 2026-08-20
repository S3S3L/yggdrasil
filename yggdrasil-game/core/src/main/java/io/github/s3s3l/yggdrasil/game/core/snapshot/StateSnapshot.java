package io.github.s3s3l.yggdrasil.game.core.snapshot;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class StateSnapshot {

    private final JsonNode data;
    private final Class<?> type;
}
