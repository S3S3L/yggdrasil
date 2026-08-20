package io.github.s3s3l.yggdrasil.game.gdx.ui;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.s3s3l.yggdrasil.game.core.ui.UiColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UiRect;
import io.github.s3s3l.yggdrasil.game.core.ui.UiViewport;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.DrawCommand;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.FillRectCommand;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.PopClipCommand;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.PushClipCommand;
import io.github.s3s3l.yggdrasil.game.core.ui.draw.TextCommand;
import io.github.s3s3l.yggdrasil.game.gdx.font.GdxFontSystem;

/**
 * Render-thread-only LibGDX consumer for core UI draw commands.
 *
 * <p>Core UI coordinates are top-left-origin. This adapter converts them to
 * LibGDX's default bottom-left camera convention and applies nested clips in
 * viewport screen coordinates.</p>
 */
public final class GdxUiRenderer {

    private final ShapeRenderer shapes;
    private final SpriteBatch batch;
    private final GdxFontSystem fonts;
    private final Viewport viewport;
    private final UiViewport uiViewport;
    private final Deque<UiRect> clips = new ArrayDeque<>();
    private boolean drawingShapes;
    private boolean drawingText;

    public GdxUiRenderer(ShapeRenderer shapes, SpriteBatch batch, GdxFontSystem fonts, Viewport viewport,
            UiViewport uiViewport) {
        this.shapes = Objects.requireNonNull(shapes, "shapes");
        this.batch = Objects.requireNonNull(batch, "batch");
        this.fonts = Objects.requireNonNull(fonts, "fonts");
        this.viewport = Objects.requireNonNull(viewport, "viewport");
        this.uiViewport = Objects.requireNonNull(uiViewport, "uiViewport");
    }

    public void render(List<? extends DrawCommand> commands) {
        Objects.requireNonNull(commands, "commands");
        viewport.apply();
        shapes.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        try {
            for (DrawCommand command : commands) {
                render(command);
            }
            if (!clips.isEmpty()) {
                throw new IllegalStateException("unbalanced UI clip commands");
            }
        } finally {
            endDrawing();
            clips.clear();
            Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
        }
    }

    private void render(DrawCommand command) {
        if (command instanceof FillRectCommand) {
            FillRectCommand fill = (FillRectCommand) command;
            beginShapes();
            setShapeColor(fill.getColor());
            UiRect bounds = fill.getBounds();
            shapes.rect(bounds.x(), toGdxY(bounds.y(), bounds.height()), bounds.width(), bounds.height());
        } else if (command instanceof TextCommand) {
            TextCommand text = (TextCommand) command;
            beginText();
            UiColor color = text.getColor();
            fonts.draw(batch, text.getFont(), text.getText(), text.getBaseline().x(),
                    toGdxY(text.getBaseline().y(), 0), color);
        } else if (command instanceof PushClipCommand) {
            endDrawing();
            UiRect requested = ((PushClipCommand) command).getBounds();
            UiRect clip = clips.isEmpty() ? requested : clips.peek().intersection(requested);
            clips.push(clip);
            applyClip(clip);
        } else if (command instanceof PopClipCommand) {
            endDrawing();
            if (clips.isEmpty()) {
                throw new IllegalStateException("clip pop without matching push");
            }
            clips.pop();
            if (clips.isEmpty()) {
                Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
            } else {
                applyClip(clips.peek());
            }
        } else {
            throw new IllegalArgumentException("unsupported draw command: " + command.getClass().getName());
        }
    }

    private void beginShapes() {
        if (drawingText) {
            batch.end();
            drawingText = false;
        }
        if (!drawingShapes) {
            shapes.begin(ShapeRenderer.ShapeType.Filled);
            drawingShapes = true;
        }
    }

    private void beginText() {
        if (drawingShapes) {
            shapes.end();
            drawingShapes = false;
        }
        if (!drawingText) {
            batch.begin();
            drawingText = true;
        }
    }

    private void endDrawing() {
        if (drawingShapes) {
            shapes.end();
            drawingShapes = false;
        }
        if (drawingText) {
            batch.end();
            drawingText = false;
        }
    }

    private void setShapeColor(UiColor color) {
        shapes.setColor(color.red(), color.green(), color.blue(), color.alpha());
    }

    private void applyClip(UiRect clip) {
        float scaleX = viewport.getScreenWidth() / uiViewport.uiWidth();
        float scaleY = viewport.getScreenHeight() / uiViewport.uiHeight();
        int x = viewport.getScreenX() + (int) Math.floor(clip.x() * scaleX);
        int y = viewport.getScreenY() + (int) Math.floor((uiViewport.uiHeight() - clip.y() - clip.height()) * scaleY);
        int width = (int) Math.ceil(clip.width() * scaleX);
        int height = (int) Math.ceil(clip.height() * scaleY);
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(x, y, Math.max(0, width), Math.max(0, height));
    }

    private float toGdxY(float y, float height) {
        return uiViewport.uiHeight() - y - height;
    }
}
