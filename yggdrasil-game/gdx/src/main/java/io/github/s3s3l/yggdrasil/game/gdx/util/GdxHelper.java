package io.github.s3s3l.yggdrasil.game.gdx.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import io.github.s3s3l.yggdrasil.game.core.basic.color.RGBA;
import io.github.s3s3l.yggdrasil.game.gdx.runtime.GdxResources;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

public class GdxHelper {

    private GdxHelper() {
    }

    /**
     * @deprecated Use {@link GdxResources#createFont(String)} so the font is
     *             scoped to an instance runtime. Call this legacy method only from
     *             the LibGDX render thread and dispose its result yourself.
     */
    @Deprecated
    public static BitmapFont getFont(String fontPath) {
        if (StringUtils.isEmpty(fontPath)) {
            return new BitmapFont();
        }
        return new BitmapFont(Gdx.files.classpath(fontPath));
    }

    public static BitmapFont getFont(GdxResources resources, String fontPath) {
        return resources.createFont(fontPath);
    }

    public static Color getColor(RGBA rgba) {
        return new Color(rgba.getR(), rgba.getG(), rgba.getB(), rgba.getA());
    }

}
