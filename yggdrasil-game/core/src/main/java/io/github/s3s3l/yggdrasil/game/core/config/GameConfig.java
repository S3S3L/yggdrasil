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
public class GameConfig {
    @Builder.Default
    private WindowConfig windowConfig = new WindowConfig();
    @Builder.Default
    private FrameConfig frameConfig = new FrameConfig();
}
