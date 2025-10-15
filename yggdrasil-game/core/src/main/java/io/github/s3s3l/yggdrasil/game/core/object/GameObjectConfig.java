package io.github.s3s3l.yggdrasil.game.core.object;

import io.github.s3s3l.yggdrasil.game.core.basic.GameAtom;
import io.github.s3s3l.yggdrasil.game.core.config.Props;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GameObjectConfig {
    
    protected Class<? extends GameAtom> type;
    @Builder.Default
    @Setter(lombok.AccessLevel.NONE)
    protected Props props = new Props();
    // @Builder.Default
    // @Setter(lombok.AccessLevel.NONE)
    // protected Map<String, StateSnapshot> states = new HashMap<>();
}
