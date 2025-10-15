package io.github.s3s3l.yggdrasil.sample.game.screen;

import io.github.s3s3l.yggdrasil.game.core.config.Props;
import io.github.s3s3l.yggdrasil.game.core.object.GameObjectConfig;
import io.github.s3s3l.yggdrasil.game.core.scene.SceneConfig;
import io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot;
import io.github.s3s3l.yggdrasil.game.gdx.scene.GdxScene;
import io.github.s3s3l.yggdrasil.game.gdx.ui.text.Text;

public class MainScreen extends GdxScene {

    public MainScreen(SceneConfig config) {
        super(config);
        gameObjectManager.create(GameObjectConfig.builder()
                .type(Text.class)
                .props(new Props()
                        .addProps("x", "400")
                        .addProps("y", "300")
                        .addProps("content", "Start")
                        .addProps("color", "255,255,255,1"))
                .build(), Text.class);
    }

    @Override
    public void onWindowResize(int width, int height) {
    }

    @Override
    public void afterPropertiesSet() {
    }

    @Override
    protected void doRestore(ObjectSnapshot snapshot) {
    }

    @Override
    protected void doSnapshot(ObjectSnapshot snapshot) {
    }

    @Override
    protected void doPreRender() {
    }

}
