package io.github.s3s3l.yggdrasil.game.gdx.ui.button;

import io.github.s3s3l.yggdrasil.game.core.basic.GameObjectProps;
import io.github.s3s3l.yggdrasil.game.core.config.Props;
import io.github.s3s3l.yggdrasil.game.gdx.ui.text.TextProps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TextButtonProps implements GameObjectProps {

    private TextProps textProps;

    @Override
    public void load(@NonNull Props props) {
    }

}
