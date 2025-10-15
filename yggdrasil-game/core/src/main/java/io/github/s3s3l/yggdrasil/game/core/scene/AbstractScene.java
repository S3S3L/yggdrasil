package io.github.s3s3l.yggdrasil.game.core.scene;

import io.github.s3s3l.yggdrasil.game.core.basic.AbstractGameAtom;
import io.github.s3s3l.yggdrasil.game.core.object.GameObjectManager;

public abstract class AbstractScene extends AbstractGameAtom<SceneConfig> implements Scene {

    public AbstractScene(SceneConfig config) {
        super(config);
    }

    protected final GameObjectManager gameObjectManager = new GameObjectManager();

    @Override
    protected void doCalculate(float delta) {
        gameObjectManager.doCalculate(delta);
    }

    @Override
    protected void doRender() {
        gameObjectManager.doRender();
    }

}
