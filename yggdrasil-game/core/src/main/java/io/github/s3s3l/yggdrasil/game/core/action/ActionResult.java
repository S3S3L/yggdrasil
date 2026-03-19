package io.github.s3s3l.yggdrasil.game.core.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 动作执行结果
 */
@Getter
@RequiredArgsConstructor
public class ActionResult {

    /**
     * 是否成功
     */
    private final boolean success;

    /**
     * 结果描述
     */
    private final String message;

    /**
     * 实际消耗的行动力
     */
    private final int apCost;

    /**
     * 实际消耗的生命值
     */
    private final int hpCost;

    /**
     * 动作产生的效果
     */
    private final Object effect;

    /**
     * 创建成功结果
     *
     * @param message 消息
     * @return 成功结果
     */
    public static ActionResult success(String message) {
        return new ActionResult(true, message, 0, 0, null);
    }

    /**
     * 创建成功结果（带消耗）
     *
     * @param message 消息
     * @param apCost  行动力消耗
     * @param hpCost  生命值消耗
     * @return 成功结果
     */
    public static ActionResult success(String message, int apCost, int hpCost) {
        return new ActionResult(true, message, apCost, hpCost, null);
    }

    /**
     * 创建成功结果（带效果）
     *
     * @param message 消息
     * @param effect  效果
     * @return 成功结果
     */
    public static ActionResult success(String message, Object effect) {
        return new ActionResult(true, message, 0, 0, effect);
    }

    /**
     * 创建成功结果（带消耗和效果）
     *
     * @param message 消息
     * @param apCost  行动力消耗
     * @param hpCost  生命值消耗
     * @param effect  效果
     * @return 成功结果
     */
    public static ActionResult success(String message, int apCost, int hpCost, Object effect) {
        return new ActionResult(true, message, apCost, hpCost, effect);
    }

    /**
     * 创建失败结果
     *
     * @param message 消息
     * @return 失败结果
     */
    public static ActionResult failure(String message) {
        return new ActionResult(false, message, 0, 0, null);
    }

    /**
     * 创建失败结果（带原因）
     *
     * @param message 消息
     * @param reason  原因
     * @return 失败结果
     */
    public static ActionResult failure(String message, String reason) {
        return new ActionResult(false, message + ": " + reason, 0, 0, null);
    }
}
