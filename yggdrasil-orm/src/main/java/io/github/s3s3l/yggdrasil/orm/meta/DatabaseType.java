package io.github.s3s3l.yggdrasil.orm.meta;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseType {
    private String type;
    private List<String> args;
    @Builder.Default
    private boolean primary = false;
    @Builder.Default
    private boolean notNull = false;
}
