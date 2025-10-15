package io.github.s3s3l.yggdrasil.game.gdx.ui.text;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Align;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.object.AbstractGameObject;
import io.github.s3s3l.yggdrasil.game.core.object.GameObjectConfig;
import io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot;
import io.github.s3s3l.yggdrasil.game.core.state.State;
import io.github.s3s3l.yggdrasil.game.gdx.util.GdxHelper;

public class Text extends AbstractGameObject {

    /**
     * 相对父对象的位置
     */
    private final State<Vector2D> location = createState("location", new Vector2D(0f, 0f), Vector2D.class);
    private final State<String> content = createState("content", "Start", String.class);
    private final TextProps textProps = new TextProps();

    private BitmapFont font;

    public Text(GameObjectConfig config) {
        super(config);
        textProps.load(props);
        font = GdxHelper.getFont(textProps.getFont());
        font.getData().setScale(textProps.getSize());
        font.setColor(GdxHelper.getColor(textProps.getColor()));
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
        var l = location.getClone();
        font.draw(GdxHelper.BATCH, content.getClone(), l.x, l.y, 40, Align.center, false);
        GdxHelper.BATCH.end();
    }

    @Override
    public void afterPropertiesSet() {
        location.update(l -> {
            l.x = props.getFloat("x", l.x);
            l.y = props.getFloat("y", l.y);
            return l;
        });
        content.update(v -> props.getString("content", content.getClone()));
    }

    @Override
    public void onWindowResize(int width, int height) {
    }

}
