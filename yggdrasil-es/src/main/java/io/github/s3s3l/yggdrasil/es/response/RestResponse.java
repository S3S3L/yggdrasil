package io.github.s3s3l.yggdrasil.es.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse<T> {
    private Response<T> response;

    @JsonProperty("is_partial")
    private boolean isPartial;
    @JsonProperty("is_running")
    private boolean isRunning;
    @JsonProperty("start_time_in_millis")
    private long startTimeinMillis;
    @JsonProperty("expiration_time_in_millis")
    private long expirationTimeInMillis;
    @JsonProperty("completion_time_in_millis")
    private long completionTimeInMillis;
}
