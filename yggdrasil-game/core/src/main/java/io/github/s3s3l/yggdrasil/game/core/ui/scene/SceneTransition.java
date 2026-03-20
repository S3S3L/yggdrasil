package io.github.s3s3l.yggdrasil.game.core.ui.scene;

/**
 * 场景过渡动画基类
 */
public abstract class SceneTransition {

    private float duration;
    private float elapsed;
    private boolean finished;
    private UIScene fromScene;
    private UIScene toScene;

    /**
     * 创建过渡动画
     *
     * @param duration 过渡时长（秒）
     */
    protected SceneTransition(float duration) {
        this.duration = duration;
        this.elapsed = 0;
        this.finished = false;
    }

    /**
     * 获取过渡时长
     *
     * @return 时长（秒）
     */
    public float getDuration() {
        return duration;
    }

    /**
     * 设置过渡时长
     *
     * @param duration 时长（秒）
     */
    public void setDuration(float duration) {
        this.duration = duration;
    }

    /**
     * 获取已过去的时间
     *
     * @return 已过去的时间（秒）
     */
    public float getElapsed() {
        return elapsed;
    }

    /**
     * 获取过渡进度（0-1）
     *
     * @return 进度
     */
    public float getProgress() {
        if (duration <= 0) {
            return 1;
        }
        return Math.min(1, elapsed / duration);
    }

    /**
     * 检查过渡是否完成
     *
     * @return true 如果完成
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * 获取源场景
     *
     * @return 源场景
     */
    public UIScene getFromScene() {
        return fromScene;
    }

    /**
     * 获取目标场景
     *
     * @return 目标场景
     */
    public UIScene getToScene() {
        return toScene;
    }

    /**
     * 开始过渡
     *
     * @param fromScene 源场景
     * @param toScene   目标场景
     */
    public void begin(UIScene fromScene, UIScene toScene) {
        this.fromScene = fromScene;
        this.toScene = toScene;
        this.elapsed = 0;
        this.finished = false;
        onBegin(fromScene, toScene);
    }

    /**
     * 更新过渡
     *
     * @param deltaTime 时间增量
     */
    public void update(float deltaTime) {
        if (finished) {
            return;
        }

        elapsed += deltaTime;

        if (elapsed >= duration) {
            elapsed = duration;
            finished = true;
            onFinish();
        } else {
            onUpdate(getProgress());
        }
    }

    /**
     * 取消过渡
     */
    public void cancel() {
        onCancel();
        finished = true;
    }

    /**
     * 渲染过渡
     */
    public abstract void render();

    /**
     * 过渡开始时调用
     *
     * @param fromScene 源场景
     * @param toScene   目标场景
     */
    protected void onBegin(UIScene fromScene, UIScene toScene) {
        // 子类可重写
    }

    /**
     * 过渡更新时调用
     *
     * @param progress 过渡进度（0-1）
     */
    protected void onUpdate(float progress) {
        // 子类可重写
    }

    /**
     * 过渡完成时调用
     */
    protected void onFinish() {
        // 子类可重写
    }

    /**
     * 过渡取消时调用
     */
    protected void onCancel() {
        // 子类可重写
    }
}
