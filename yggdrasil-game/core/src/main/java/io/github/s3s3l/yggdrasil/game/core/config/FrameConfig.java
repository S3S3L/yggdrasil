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
public class FrameConfig {
    @Builder.Default
    private FrameType type = FrameType.FIXED;
    @Builder.Default
    private int fps = 60;
}
