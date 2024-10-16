package io.github.s3s3l.yggdrasil.otel.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Sdk {
    private String language;
    private String name;
    private String version;
}
