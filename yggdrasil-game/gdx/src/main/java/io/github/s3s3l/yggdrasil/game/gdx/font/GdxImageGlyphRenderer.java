package io.github.s3s3l.yggdrasil.game.gdx.font;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.s3s3l.yggdrasil.game.core.font.ImageGlyph;
import io.github.s3s3l.yggdrasil.game.core.font.ImageGlyphSource;
import io.github.s3s3l.yggdrasil.game.core.ui.UiColor;

/** A LibGDX image-glyph extension that can draw the images it owns. */
public interface GdxImageGlyphRenderer extends ImageGlyphSource {

    void draw(SpriteBatch batch, ImageGlyph glyph, float x, float baseline, UiColor color);
}
