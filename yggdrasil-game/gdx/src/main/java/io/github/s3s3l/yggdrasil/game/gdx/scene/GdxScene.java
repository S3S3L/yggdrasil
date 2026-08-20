package io.github.s3s3l.yggdrasil.game.gdx.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.s3s3l.yggdrasil.game.core.scene.AbstractScene;
import io.github.s3s3l.yggdrasil.game.core.scene.SceneConfig;
import io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot;
import io.github.s3s3l.yggdrasil.game.core.snapshot.SnapshotManager;
import io.github.s3s3l.yggdrasil.game.gdx.runtime.GdxResources;
import io.github.s3s3l.yggdrasil.game.gdx.runtime.GdxRuntime;
import io.github.s3s3l.yggdrasil.game.gdx.runtime.GdxRuntimeAware;

public abstract class GdxScene extends AbstractScene implements Screen, GdxRuntimeAware {

    protected final Camera camera;
    protected final Viewport viewport;
    private GdxRuntime runtime;

    public GdxScene(SceneConfig config) {
        super(config);

        float width = props.getFloat("width", config.getDefaultWidth());
        float height = props.getFloat("height", config.getDefaultHeight());
        float cameraWidth = props.getFloat("camera.width", width);
        float cameraHeight = props.getFloat("camera.height", height);
        camera = new OrthographicCamera(cameraWidth, cameraHeight);
        camera.position.set(width / 2f, height / 2f, 0);
        camera.update();
        viewport = new FitViewport(width, height, camera);
    }

    // Screen methods

    /**
     * 每帧调用，delta为距离上次调用的时间间隔（秒）
     */
    @Override
    public void render(float delta) {
        attachRuntimeToObjects();
        runtime().runFrame(this, delta, () -> {
            attachRuntimeToObjects();
            Gdx.gl.glClearColor(0f, 0f, 0f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        });
    }

    /**
     * 窗口尺寸变化时调用
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false); // 更新视口
    }

    /**
     * Screen被切换为当前显示时调用
     */
    @Override
    public void show() {
        ObjectSnapshot latestSnapshot = SnapshotManager.get().getLatestSnapshot(this);
        if (latestSnapshot != null) {
            restore(latestSnapshot);
        }
    }

    /**
     * Screen不再是当前显示时调用
     */
    @Override
    public void hide() {
        SnapshotManager.get().saveSnapshot(snapshot());
    }

    /**
     * 游戏暂停时调用（如窗口失去焦点）
     */
    @Override
    public void pause() {
    }

    /**
     * 游戏恢复时调用（如窗口重新获得焦点）
     */
    @Override
    public void resume() {
    }

    /**
     * 释放资源时调用（如游戏退出时）
     */
    @Override
    public void dispose() {
        finalizeResource();
    }

    @Override
    public void attachRuntime(GdxRuntime runtime) {
        if (this.runtime != null && this.runtime != runtime) {
            throw new IllegalStateException("A scene cannot be attached to more than one runtime");
        }
        this.runtime = runtime;
    }

    protected final GdxRuntime runtime() {
        if (runtime == null) {
            throw new IllegalStateException("Scene must be created by a GdxGame runtime");
        }
        return runtime;
    }

    protected final GdxResources resources() {
        return runtime().resources();
    }

    private void attachRuntimeToObjects() {
        for (var object : gameObjectManager.activeObjects()) {
            if (object instanceof GdxRuntimeAware runtimeAware) {
                runtimeAware.attachRuntime(runtime());
            }
        }
    }

}
