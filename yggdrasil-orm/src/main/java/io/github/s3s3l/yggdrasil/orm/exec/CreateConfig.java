package io.github.s3s3l.yggdrasil.orm.exec;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateConfig {
    @Builder.Default
    private boolean force = false;
    @Builder.Default
    private boolean dropFirst = false;
}
