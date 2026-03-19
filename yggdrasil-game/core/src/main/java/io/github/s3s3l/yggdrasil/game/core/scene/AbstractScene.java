package io.github.s3s3l.yggdrasil.game.core.scene;

import io.github.s3s3l.yggdrasil.game.core.basic.AbstractGameAtom;
import io.github.s3s3l.yggdrasil.game.core.object.GameObjectManager;

/**
 * 场景抽象基类
 * 提供场景的通用实现，包括游戏对象管理
 */
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

    @Override
    public void show() {
        // 默认实现，子类可重写
    }

    @Override
    public void hide() {
        // 默认实现，子类可重写
    }

    @Override
    public void update(float delta) {
        // 默认实现，子类可重写
        calculate(delta);
    }
}
