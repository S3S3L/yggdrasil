package io.github.s3s3l.yggdrasil.game.core.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WindowConfig {
    @Builder.Default
    private boolean isWindowed = true;
    @Builder.Default
    private int width = 800;
    @Builder.Default
    private int height = 600;
    @Builder.Default
    private boolean resizable = false;
}
