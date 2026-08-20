package io.github.s3s3l.yggdrasil.game.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class KeyEvent extends BaseEvent {
    protected int keyCode;
    protected boolean repeated;
}
