package io.github.s3s3l.yggdrasil.game.core.ui.input;

/**
 * 输入处理器接口
 * 处理各种输入事件
 */
public interface InputProcessor {

    /**
     * 触摸按下时调用
     *
     * @param x         触摸 X 坐标
     * @param y         触摸 Y 坐标
     * @param pointerId 指针 ID
     * @return true 如果事件已处理
     */
    boolean touchDown(float x, float y, int pointerId);

    /**
     * 触摸释放时调用
     *
     * @param x         触摸 X 坐标
     * @param y         触摸 Y 坐标
     * @param pointerId 指针 ID
     * @return true 如果事件已处理
     */
    boolean touchUp(float x, float y, int pointerId);

    /**
     * 触摸拖动时调用
     *
     * @param x         触摸 X 坐标
     * @param y         触摸 Y 坐标
     * @param pointerId 指针 ID
     * @return true 如果事件已处理
     */
    boolean touchDragged(float x, float y, int pointerId);

    /**
     * 按键按下时调用
     *
     * @param keyCode 按键代码
     * @return true 如果事件已处理
     */
    boolean keyDown(int keyCode);

    /**
     * 按键释放时调用
     *
     * @param keyCode 按键代码
     * @return true 如果事件已处理
     */
    boolean keyUp(int keyCode);

    /**
     * 输入处理器适配器类
     * 提供空实现，方便只重写需要的方法
     */
    abstract class Adapter implements InputProcessor {
        @Override
        public boolean touchDown(float x, float y, int pointerId) {
            return false;
        }

        @Override
        public boolean touchUp(float x, float y, int pointerId) {
            return false;
        }

        @Override
        public boolean touchDragged(float x, float y, int pointerId) {
            return false;
        }

        @Override
        public boolean keyDown(int keyCode) {
            return false;
        }

        @Override
        public boolean keyUp(int keyCode) {
            return false;
        }
    }
}
