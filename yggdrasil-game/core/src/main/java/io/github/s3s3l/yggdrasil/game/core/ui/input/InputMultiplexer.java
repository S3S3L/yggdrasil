package io.github.s3s3l.yggdrasil.game.core.ui.input;

import java.util.ArrayList;
import java.util.List;

/**
 * 输入复用器
 * 将输入事件分发到多个输入处理器
 */
public class InputMultiplexer implements InputProcessor {

    private final List<InputProcessor> processors;
    private boolean catchUnhandled;

    /**
     * 创建输入复用器
     */
    public InputMultiplexer() {
        this.processors = new ArrayList<>();
        this.catchUnhandled = false;
    }

    /**
     * 创建输入复用器
     *
     * @param catchUnhandled 是否捕获未处理的事件
     */
    public InputMultiplexer(boolean catchUnhandled) {
        this();
        this.catchUnhandled = catchUnhandled;
    }

    /**
     * 添加输入处理器
     *
     * @param processor 处理器
     */
    public void addProcessor(InputProcessor processor) {
        if (processor != null && !processors.contains(processor)) {
            processors.add(processor);
        }
    }

    /**
     * 添加输入处理器到指定位置
     *
     * @param index     索引位置
     * @param processor 处理器
     */
    public void addProcessor(int index, InputProcessor processor) {
        if (processor != null) {
            processors.add(index, processor);
        }
    }

    /**
     * 移除输入处理器
     *
     * @param processor 处理器
     */
    public void removeProcessor(InputProcessor processor) {
        processors.remove(processor);
    }

    /**
     * 清除所有处理器
     */
    public void clearProcessors() {
        processors.clear();
    }

    /**
     * 获取处理器列表
     *
     * @return 处理器列表
     */
    public List<InputProcessor> getProcessors() {
        return new ArrayList<>(processors);
    }

    /**
     * 设置是否捕获未处理的事件
     *
     * @param catchUnhandled true 捕获未处理事件
     */
    public void setCatchUnhandled(boolean catchUnhandled) {
        this.catchUnhandled = catchUnhandled;
    }

    /**
     * 检查是否捕获未处理的事件
     *
     * @return true 如果捕获未处理事件
     */
    public boolean isCatchUnhandled() {
        return catchUnhandled;
    }

    @Override
    public boolean touchDown(float x, float y, int pointerId) {
        boolean handled = false;
        for (InputProcessor processor : processors) {
            if (processor.touchDown(x, y, pointerId)) {
                handled = true;
                if (!catchUnhandled) {
                    return true;
                }
            }
        }
        return handled;
    }

    @Override
    public boolean touchUp(float x, float y, int pointerId) {
        boolean handled = false;
        for (InputProcessor processor : processors) {
            if (processor.touchUp(x, y, pointerId)) {
                handled = true;
                if (!catchUnhandled) {
                    return true;
                }
            }
        }
        return handled;
    }

    @Override
    public boolean touchDragged(float x, float y, int pointerId) {
        boolean handled = false;
        for (InputProcessor processor : processors) {
            if (processor.touchDragged(x, y, pointerId)) {
                handled = true;
                if (!catchUnhandled) {
                    return true;
                }
            }
        }
        return handled;
    }

    @Override
    public boolean keyDown(int keyCode) {
        boolean handled = false;
        for (InputProcessor processor : processors) {
            if (processor.keyDown(keyCode)) {
                handled = true;
                if (!catchUnhandled) {
                    return true;
                }
            }
        }
        return handled;
    }

    @Override
    public boolean keyUp(int keyCode) {
        boolean handled = false;
        for (InputProcessor processor : processors) {
            if (processor.keyUp(keyCode)) {
                handled = true;
                if (!catchUnhandled) {
                    return true;
                }
            }
        }
        return handled;
    }
}
