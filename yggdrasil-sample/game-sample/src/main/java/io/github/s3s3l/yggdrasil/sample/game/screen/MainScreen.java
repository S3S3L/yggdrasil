package io.github.s3s3l.yggdrasil.sample.game.screen;

import com.badlogic.gdx.Gdx;

import io.github.s3s3l.yggdrasil.game.core.config.Props;
import io.github.s3s3l.yggdrasil.game.core.event.ClickEvent;
import io.github.s3s3l.yggdrasil.game.core.event.EventCenter;
import io.github.s3s3l.yggdrasil.game.core.event.EventContext;
import io.github.s3s3l.yggdrasil.game.core.event.EventListener;
import io.github.s3s3l.yggdrasil.game.core.event.EventPhase;
import io.github.s3s3l.yggdrasil.game.core.event.EventResult;
import io.github.s3s3l.yggdrasil.game.core.event.EventType;
import io.github.s3s3l.yggdrasil.game.core.event.input.HitTester;
import io.github.s3s3l.yggdrasil.game.core.object.GameObjectConfig;
import io.github.s3s3l.yggdrasil.game.core.scene.SceneConfig;
import io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot;
import io.github.s3s3l.yggdrasil.game.gdx.input.GdxEventInputProcessor;
import io.github.s3s3l.yggdrasil.game.gdx.input.Vector2DHitTester;
import io.github.s3s3l.yggdrasil.game.gdx.scene.GdxScene;
import io.github.s3s3l.yggdrasil.game.gdx.ui.text.Text;

public class MainScreen extends GdxScene {

    public MainScreen(SceneConfig config) {
        super(config);
        HitTester hitTester = new Vector2DHitTester(gameObjectManager::activeObjects);
        Gdx.input.setInputProcessor(new GdxEventInputProcessor(EventCenter.get(), hitTester));

        Text text = gameObjectManager.create(GameObjectConfig.builder()
                .type(Text.class)
                .props(new Props()
                        .addProps("x", "400")
                        .addProps("y", "300")
                        .addProps("content", "Start")
                        .addProps("color", "255,255,255,1"))
                .build(), Text.class);
        text.on(EventType.POINTER_UP, new EventListener<ClickEvent<?>>() {
            @Override
            public EventResult handle(EventContext<ClickEvent<?>> context) {
                System.out.println("Clicked");
                return EventResult.CONTINUE;
            }
        }, opts -> {
            opts.setPhase(EventPhase.TARGET);
        });
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
