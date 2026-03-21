package io.github.s3s3l.yggdrasil.game.core.demo.input;

import io.github.s3s3l.yggdrasil.game.core.demo.entity.GameAction;

/**
 * 默认输入配置
 * 提供默认的游戏控制绑定
 *
 * 注意：此类使用 LibGDX 的 Input.Keys 常量
 * 如果 LibGDX 不在类路径中，需要手动指定键码值
 */
public class DefaultInputConfig {

    // 常用键码常量（LibGDX 兼容值）
    public static final int KEY_W = 87;
    public static final int KEY_S = 83;
    public static final int KEY_A = 65;
    public static final int KEY_D = 68;
    public static final int KEY_J = 74;
    public static final int KEY_K = 75;
    public static final int KEY_L = 76;
    public static final int KEY_I = 73;
    public static final int KEY_F = 70;
    public static final int KEY_ESCAPE = 131;
    public static final int KEY_P = 80;
    public static final int KEY_ENTER = 66;
    public static final int KEY_SPACE = 62;
    public static final int KEY_BACKSPACE = 127;
    public static final int KEY_UP = 20;
    public static final int KEY_DOWN = 21;
    public static final int KEY_LEFT = 22;
    public static final int KEY_RIGHT = 23;

    /**
     * 创建默认输入配置
     *
     * @param inputActionMap 输入动作映射
     */
    public static void applyDefaultBindings(InputActionMap inputActionMap) {
        if (inputActionMap == null) {
            return;
        }

        // 移动
        inputActionMap.bind(KEY_W, GameAction.MOVE_UP);
        inputActionMap.bind(KEY_S, GameAction.MOVE_DOWN);
        inputActionMap.bind(KEY_A, GameAction.MOVE_LEFT);
        inputActionMap.bind(KEY_D, GameAction.MOVE_RIGHT);

        // 攻击
        inputActionMap.bind(KEY_J, GameAction.ATTACK);

        // 技能
        inputActionMap.bind(KEY_K, GameAction.SKILL_1);
        inputActionMap.bind(KEY_L, GameAction.SKILL_2);
        inputActionMap.bind(KEY_I, GameAction.SKILL_3);

        // 交互
        inputActionMap.bind(KEY_F, GameAction.INTERACT);

        // 暂停
        inputActionMap.bind(KEY_ESCAPE, GameAction.PAUSE);
        inputActionMap.bind(KEY_P, GameAction.PAUSE);

        // UI 导航
        inputActionMap.bind(KEY_ENTER, GameAction.CONFIRM);
        inputActionMap.bind(KEY_SPACE, GameAction.CONFIRM);
        inputActionMap.bind(KEY_BACKSPACE, GameAction.CANCEL);
    }

    /**
     * 创建方向键配置（替代 WASD）
     *
     * @param inputActionMap 输入动作映射
     */
    public static void applyArrowKeys(InputActionMap inputActionMap) {
        if (inputActionMap == null) {
            return;
        }

        inputActionMap.bind(KEY_UP, GameAction.MOVE_UP);
        inputActionMap.bind(KEY_DOWN, GameAction.MOVE_DOWN);
        inputActionMap.bind(KEY_LEFT, GameAction.MOVE_LEFT);
        inputActionMap.bind(KEY_RIGHT, GameAction.MOVE_RIGHT);
    }
}
