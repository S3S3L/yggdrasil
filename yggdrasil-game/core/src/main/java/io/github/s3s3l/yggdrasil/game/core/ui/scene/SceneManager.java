package io.github.s3s3l.yggdrasil.game.core.ui.scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 场景管理器
 * 管理场景的切换、栈操作和生命周期
 */
public class SceneManager {

    private static SceneManager instance;

    private final Map<String, UIScene> scenes;
    private final Stack<UIScene> sceneStack;
    private UIScene currentScene;
    private UIScene previousScene;
    private SceneTransition currentTransition;
    private final List<SceneChangeListener> listeners;

    /**
     * 私有构造函数
     */
    private SceneManager() {
        this.scenes = new HashMap<>();
        this.sceneStack = new Stack<>();
        this.listeners = new ArrayList<>();
    }

    /**
     * 获取单例实例
     *
     * @return SceneManager 实例
     */
    public static synchronized SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    /**
     * 重置单例（用于测试）
     */
    public static synchronized void resetInstance() {
        if (instance != null) {
            instance.scenes.clear();
            instance.sceneStack.clear();
            instance.currentScene = null;
            instance.previousScene = null;
            instance.currentTransition = null;
            instance.listeners.clear();
        }
        instance = null;
    }

    /**
     * 注册场景
     *
     * @param scene 场景
     */
    public void registerScene(UIScene scene) {
        if (scene != null && scene.getName() != null) {
            scenes.put(scene.getName(), scene);
        }
    }

    /**
     * 注销场景
     *
     * @param sceneName 场景名称
     */
    public void unregisterScene(String sceneName) {
        UIScene scene = scenes.remove(sceneName);
        if (scene != null && scene.isActive()) {
            scene.onExit();
        }
    }

    /**
     * 获取场景
     *
     * @param sceneName 场景名称
     * @return 场景，如果没有则返回 null
     */
    public UIScene getScene(String sceneName) {
        return scenes.get(sceneName);
    }

    /**
     * 获取所有已注册的场景
     *
     * @return 场景映射
     */
    public Map<String, UIScene> getScenes() {
        return Collections.unmodifiableMap(scenes);
    }

    /**
     * 检查是否有场景
     *
     * @param sceneName 场景名称
     * @return true 如果有该场景
     */
    public boolean hasScene(String sceneName) {
        return scenes.containsKey(sceneName);
    }

    /**
     * 切换场景
     *
     * @param sceneName 场景名称
     */
    public void changeScene(String sceneName) {
        changeScene(sceneName, null);
    }

    /**
     * 切换场景
     *
     * @param sceneName  场景名称
     * @param transition 过渡动画
     */
    public void changeScene(String sceneName, SceneTransition transition) {
        UIScene nextScene = scenes.get(sceneName);
        if (nextScene == null) {
            return;
        }

        if (currentScene == nextScene) {
            return;
        }

        previousScene = currentScene;

        if (currentTransition != null && !currentTransition.isFinished()) {
            currentTransition.cancel();
        }

        currentTransition = transition;

        if (currentScene != null) {
            currentScene.onExit();
        }

        if (currentTransition != null) {
            currentTransition.begin(currentScene, nextScene);
        }

        nextScene.onEnter();
        currentScene = nextScene;

        // 清空场景栈
        sceneStack.clear();

        notifySceneChanged(previousScene, currentScene);
    }

    /**
     * 压入场景（栈方式）
     *
     * @param sceneName 场景名称
     */
    public void pushScene(String sceneName) {
        pushScene(sceneName, null);
    }

    /**
     * 压入场景（栈方式）
     *
     * @param sceneName  场景名称
     * @param transition 过渡动画
     */
    public void pushScene(String sceneName, SceneTransition transition) {
        UIScene nextScene = scenes.get(sceneName);
        if (nextScene == null) {
            return;
        }

        if (currentScene != null) {
            currentScene.onPause();
            sceneStack.push(currentScene);
        }

        previousScene = currentScene;
        currentTransition = transition;

        if (currentTransition != null) {
            currentTransition.begin(currentScene, nextScene);
        }

        nextScene.onEnter();
        currentScene = nextScene;

        notifySceneChanged(previousScene, currentScene);
    }

    /**
     * 弹出场景
     *
     * @return 弹出的场景
     */
    public UIScene popScene() {
        return popScene(null);
    }

    /**
     * 弹出场景
     *
     * @param transition 过渡动画
     * @return 弹出的场景
     */
    public UIScene popScene(SceneTransition transition) {
        if (sceneStack.isEmpty()) {
            return null;
        }

        UIScene exitingScene = currentScene;
        if (exitingScene != null) {
            exitingScene.onExit();
        }

        currentScene = sceneStack.pop();

        previousScene = exitingScene;
        currentTransition = transition;

        if (currentTransition != null) {
            currentTransition.begin(exitingScene, currentScene);
        }

        currentScene.onResume();

        notifySceneChanged(previousScene, currentScene);

        return exitingScene;
    }

    /**
     * 获取当前场景
     *
     * @return 当前场景
     */
    public UIScene getCurrentScene() {
        return currentScene;
    }

    /**
     * 获取当前场景名称
     *
     * @return 当前场景名称
     */
    public String getCurrentSceneName() {
        return currentScene != null ? currentScene.getName() : null;
    }

    /**
     * 获取上一个场景
     *
     * @return 上一个场景
     */
    public UIScene getPreviousScene() {
        return previousScene;
    }

    /**
     * 查看栈顶场景（不弹出）
     *
     * @return 栈顶场景
     */
    public UIScene peekScene() {
        return sceneStack.isEmpty() ? null : sceneStack.peek();
    }

    /**
     * 获取场景栈大小
     *
     * @return 栈大小
     */
    public int getStackSize() {
        return sceneStack.size();
    }

    /**
     * 清除场景栈
     */
    public void clearStack() {
        sceneStack.clear();
    }

    /**
     * 更新场景管理器
     *
     * @param deltaTime 时间增量
     */
    public void update(float deltaTime) {
        if (currentTransition != null && !currentTransition.isFinished()) {
            currentTransition.update(deltaTime);
            if (currentTransition.isFinished()) {
                currentTransition = null;
            }
        }

        if (currentScene != null && !currentScene.isPaused()) {
            currentScene.update(deltaTime);
        }
    }

    /**
     * 渲染当前场景
     */
    public void render() {
        if (currentScene != null) {
            currentScene.render();
        }

        if (currentTransition != null) {
            currentTransition.render();
        }
    }

    /**
     * 添加场景变化监听器
     *
     * @param listener 监听器
     */
    public void addSceneChangeListener(SceneChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * 移除场景变化监听器
     *
     * @param listener 监听器
     */
    public void removeSceneChangeListener(SceneChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * 通知场景变化
     *
     * @param oldScene 旧场景
     * @param newScene 新场景
     */
    private void notifySceneChanged(UIScene oldScene, UIScene newScene) {
        for (SceneChangeListener listener : listeners) {
            try {
                listener.onSceneChanged(oldScene, newScene);
            } catch (Exception e) {
                // 忽略监听器异常
            }
        }
    }

    /**
     * 清除所有场景
     */
    public void clearAll() {
        for (UIScene scene : scenes.values()) {
            if (scene.isActive()) {
                scene.onExit();
            }
        }
        scenes.clear();
        sceneStack.clear();
        currentScene = null;
        previousScene = null;
        currentTransition = null;
    }

    /**
     * 场景变化监听器接口
     */
    public interface SceneChangeListener {
        /**
         * 场景变化时调用
         *
         * @param oldScene 旧场景
         * @param newScene 新场景
         */
        void onSceneChanged(UIScene oldScene, UIScene newScene);
    }
}
