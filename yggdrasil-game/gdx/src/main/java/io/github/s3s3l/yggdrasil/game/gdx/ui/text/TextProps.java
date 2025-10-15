package io.github.s3s3l.yggdrasil.game.gdx.ui.text;

import io.github.s3s3l.yggdrasil.game.core.basic.GameObjectProps;
import io.github.s3s3l.yggdrasil.game.core.basic.color.RGBA;
import io.github.s3s3l.yggdrasil.game.core.config.Props;
import io.github.s3s3l.yggdrasil.game.gdx.consts.Align;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TextProps implements GameObjectProps {
    @Builder.Default
    private Align align = Align.LEFT;
    @Builder.Default
    private float width = 80;
    @Builder.Default
    private int size = 1;
    private String font;
    private RGBA color;

    @Override
    public void load(@NonNull Props props) {
        this.align = Align.fromString(props.getString("align", align.name()));
        this.width = props.getFloat("width", width);
        this.size = props.getInt("size", size);
        this.font = props.getString("font", null);
        this.color = RGBA.fromString(props.getString("color", "0,0,0,1"));
    }
}
