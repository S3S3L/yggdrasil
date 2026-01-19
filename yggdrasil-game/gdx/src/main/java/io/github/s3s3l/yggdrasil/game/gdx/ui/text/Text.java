package io.github.s3s3l.yggdrasil.game.gdx.ui.text;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;

import io.github.s3s3l.yggdrasil.game._2d.object.GameObject2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Rectangle2D;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.object.GameObjectConfig;
import io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot;
import io.github.s3s3l.yggdrasil.game.core.state.State;
import io.github.s3s3l.yggdrasil.game.gdx.util.GdxHelper;

public class Text extends GameObject2D<Rectangle2D> {

    /**
     * 相对父对象的位置
     */
    // private final State<Vector2D> location = createState("location", new
    // Vector2D(0f, 0f), Vector2D.class);
    private final State<String> content = createState("content", "", String.class);
    private final TextProps textProps = new TextProps();

    private BitmapFont font;

    public Text(GameObjectConfig config) {
        super(config);
        textProps.load(props);
    }

    @Override
    protected void doRestore(ObjectSnapshot snapshot) {
    }

    @Override
    protected void doSnapshot(ObjectSnapshot snapshot) {
    }

    @Override
    protected void doCalculate(float delta) {
    }

    @Override
    protected void doPreRender() {
    }

    @Override
    protected void doRender() {
        GdxHelper.BATCH.begin();
        Rectangle2D rect = shape.getClone();
        var l = rect.getTopLeft();
        font.draw(GdxHelper.BATCH, content.getClone(), l.x, l.y, rect.getWidth(), Align.center, false);
        GdxHelper.BATCH.end();
    }

    @Override
    public void afterPropertiesSet() {
        font = GdxHelper.getFont(textProps.getFont());
        font.getData().setScale(textProps.getSize());
        font.setColor(GdxHelper.getColor(textProps.getColor()));
        this.shape = createState("shape",
                new Rectangle2D(new Vector2D(props.getFloat("x", 0f), props.getFloat("y", 0f)), textProps.getWidth(),
                        font.getCapHeight()),
                Rectangle2D.class);
        content.update(v -> props.getString("content", content.getClone()));
    }

    @Override
    public void onWindowResize(int width, int height) {
    }

}
