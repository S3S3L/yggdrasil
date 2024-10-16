package io.github.s3s3l.yggdrasil.otel.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    private Service service;
    private Telemetry telemetry;
}
