package io.github.s3s3l.yggdrasil.game.core.ability;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 效果应用结果
 */
@Getter
@RequiredArgsConstructor
public class EffectResult {

    /**
     * 是否成功
     */
    private final boolean success;

    /**
     * 结果描述
     */
    private final String message;

    /**
     * 效果值
     */
    private final int value;

    /**
     * 创建成功结果
     *
     * @param message 消息
     * @return 成功结果
     */
    public static EffectResult success(String message) {
        return new EffectResult(true, message, 0);
    }

    /**
     * 创建成功结果（带值）
     *
     * @param message 消息
     * @param value   值
     * @return 成功结果
     */
    public static EffectResult success(String message, int value) {
        return new EffectResult(true, message, value);
    }

    /**
     * 创建失败结果
     *
     * @param message 消息
     * @return 失败结果
     */
    public static EffectResult failure(String message) {
        return new EffectResult(false, message, 0);
    }
}
