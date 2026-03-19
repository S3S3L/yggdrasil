package io.github.s3s3l.yggdrasil.game.core.scene;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot;
import io.github.s3s3l.yggdrasil.game.core.snapshot.SnapshotManager;

/**
 * 场景管理器
 * 负责管理游戏场景的创建、切换、销毁等操作
 *
 * @author s3s3l
 */
public class SceneManager {
    private static final SceneManager INSTANCE = new SceneManager();

    /**
     * 当前场景
     */
    private Scene currentScene;

    /**
     * 场景栈，用于支持场景返回功能
     */
    private final Stack<Scene> sceneStack = new Stack<>();

    /**
     * 已创建的场景缓存
     */
    private final Map<Long, Scene> scenes = new HashMap<>();

    /**
     * 场景过渡效果
     */
    private SceneTransition transition;

    private SceneManager() {
    }

    public static SceneManager get() {
        return INSTANCE;
    }

    /**
     * 设置场景过渡效果
     *
     * @param transition 场景过渡效果
     */
    public void setTransition(SceneTransition transition) {
        this.transition = transition;
    }

    /**
     * 获取当前场景
     *
     * @return 当前场景
     */
    public Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * 创建场景
     *
     * @param <T> 场景类型
     * @param sceneClass 场景类
     * @return 创建的场景实例
     */
    public synchronized <T extends Scene> T createScene(Class<T> sceneClass) {
        try {
            T scene = sceneClass.getDeclaredConstructor().newInstance();
            scenes.put(scene.getId(), scene);
            return scene;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create scene instance", e);
        }
    }

    /**
     * 切换场景
     * 将当前场景压入栈中，然后切换到新场景
     *
     * @param newScene 新场景
     */
    public synchronized void switchScene(Scene newScene) {
        if (newScene == null) {
            return;
        }

        // 保存当前场景快照
        if (currentScene != null) {
            ObjectSnapshot snapshot = currentScene.snapshot();
            SnapshotManager.get().saveSnapshot(snapshot);

            // 调用当前场景的 hide 方法
            currentScene.hide();

            // 压入栈中
            sceneStack.push(currentScene);
        }

        // 执行过渡效果
        if (transition != null) {
            transition.onTransitionStart(currentScene, newScene);
        }

        // 切换场景
        Scene oldScene = currentScene;
        currentScene = newScene;

        // 恢复新场景快照
        ObjectSnapshot latestSnapshot = SnapshotManager.get().getLatestSnapshot(newScene);
        if (latestSnapshot != null) {
            newScene.restore(latestSnapshot);
        }

        // 调用新场景的 show 方法
        currentScene.show();

        // 执行过渡效果
        if (transition != null) {
            transition.onTransitionEnd(oldScene, newScene);
        }
    }

    /**
     * 返回上一个场景
     *
     * @return true 如果返回成功
     */
    public synchronized boolean backToPreviousScene() {
        if (sceneStack.isEmpty()) {
            return false;
        }

        // 保存当前场景快照
        if (currentScene != null) {
            ObjectSnapshot snapshot = currentScene.snapshot();
            SnapshotManager.get().saveSnapshot(snapshot);
        }

        // 弹出栈顶场景
        Scene previousScene = sceneStack.pop();

        // 执行过渡效果
        if (transition != null) {
            transition.onTransitionStart(currentScene, previousScene);
        }

        // 恢复场景
        Scene oldScene = currentScene;
        currentScene = previousScene;

        // 恢复场景快照
        ObjectSnapshot latestSnapshot = SnapshotManager.get().getLatestSnapshot(currentScene);
        if (latestSnapshot != null) {
            currentScene.restore(latestSnapshot);
        }

        // 调用场景的 show 方法
        currentScene.show();

        // 执行过渡效果
        if (transition != null) {
            transition.onTransitionEnd(oldScene, currentScene);
        }

        return true;
    }

    /**
     * 替换当前场景（不保留历史）
     *
     * @param newScene 新场景
     */
    public synchronized void replaceScene(Scene newScene) {
        if (newScene == null) {
            return;
        }

        // 保存当前场景快照
        if (currentScene != null) {
            ObjectSnapshot snapshot = currentScene.snapshot();
            SnapshotManager.get().saveSnapshot(snapshot);
        }

        // 清空场景栈
        sceneStack.clear();

        // 执行过渡效果
        if (transition != null) {
            transition.onTransitionStart(currentScene, newScene);
        }

        // 切换场景
        Scene oldScene = currentScene;
        currentScene = newScene;

        // 恢复场景快照
        ObjectSnapshot latestSnapshot = SnapshotManager.get().getLatestSnapshot(newScene);
        if (latestSnapshot != null) {
            newScene.restore(latestSnapshot);
        }

        // 调用场景的 show 方法
        currentScene.show();

        // 执行过渡效果
        if (transition != null) {
            transition.onTransitionEnd(oldScene, currentScene);
        }
    }

    /**
     * 获取场景栈深度
     *
     * @return 场景栈深度
     */
    public int getStackDepth() {
        return sceneStack.size();
    }

    /**
     * 清除场景缓存
     */
    public synchronized void clear() {
        // 保存所有场景快照
        if (currentScene != null) {
            ObjectSnapshot snapshot = currentScene.snapshot();
            SnapshotManager.get().saveSnapshot(snapshot);
        }

        for (Scene scene : scenes.values()) {
            ObjectSnapshot snapshot = scene.snapshot();
            SnapshotManager.get().saveSnapshot(snapshot);
        }

        scenes.clear();
        sceneStack.clear();
        currentScene = null;
    }
}
