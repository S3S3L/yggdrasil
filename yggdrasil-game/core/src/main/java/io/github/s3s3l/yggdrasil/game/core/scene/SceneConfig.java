package io.github.s3s3l.yggdrasil.game.core.scene;

import io.github.s3s3l.yggdrasil.game.core.object.GameObjectConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SceneConfig extends GameObjectConfig {
    @Builder.Default
    private float defaultWidth = 800;
    @Builder.Default
    private float defaultHeight = 600;
}
