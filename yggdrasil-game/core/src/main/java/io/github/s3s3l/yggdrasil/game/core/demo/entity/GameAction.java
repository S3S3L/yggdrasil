package io.github.s3s3l.yggdrasil.game.core.demo.entity;

/**
 * 游戏动作枚举
 * 用于输入映射和游戏逻辑
 */
public enum GameAction {
    /**
     * 移动
     */
    MOVE_UP,
    MOVE_DOWN,
    MOVE_LEFT,
    MOVE_RIGHT,

    /**
     * 攻击
     */
    ATTACK,

    /**
     * 特殊技能
     */
    SKILL_1,
    SKILL_2,
    SKILL_3,

    /**
     * 交互
     */
    INTERACT,

    /**
     * 跳跃
     */
    JUMP,

    /**
     * 蹲下
     */
    CROUCH,

    /**
     * 闪避
     */
    DODGE,

    /**
     * 暂停
     */
    PAUSE,

    /**
     * 确认
     */
    CONFIRM,

    /**
     * 取消
     */
    CANCEL;
}
