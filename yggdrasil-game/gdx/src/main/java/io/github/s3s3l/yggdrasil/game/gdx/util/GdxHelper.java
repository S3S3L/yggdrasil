package io.github.s3s3l.yggdrasil.game.gdx.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.s3s3l.yggdrasil.game.core.basic.color.RGBA;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

public class GdxHelper {

    public static final SpriteBatch BATCH = new SpriteBatch();
    public static final BitmapFont DEFAULT_FONT = new BitmapFont();

    public static BitmapFont getFont(String fontPath) {
        if (StringUtils.isEmpty(fontPath)) {
            return DEFAULT_FONT;
        }
        return new BitmapFont(Gdx.files.classpath(fontPath));
    }

    public static Color getColor(RGBA rgba) {
        return new Color(rgba.getR(), rgba.getG(), rgba.getB(), rgba.getA());
    }

}
