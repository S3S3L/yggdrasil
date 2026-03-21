package io.github.s3s3l.yggdrasil.game.core.demo.config;

/**
 * Demo 游戏配置
 */
public class DemoConfig {

    /**
     * 屏幕宽度
     */
    public static final int SCREEN_WIDTH = 1280;

    /**
     * 屏幕高度
     */
    public static final int SCREEN_HEIGHT = 720;

    /**
     * 目标帧率
     */
    public static final int TARGET_FPS = 60;

    /**
     * 每帧时间（秒）
     */
    public static final float DELTA_TIME = 1.0f / TARGET_FPS;

    /**
     * 玩家初始生命值
     */
    public static final float PLAYER_INITIAL_HEALTH = 100;

    /**
     * 玩家移动速度
     */
    public static final float PLAYER_MOVE_SPEED = 150;

    /**
     * 玩家攻击力
     */
    public static final float PLAYER_ATTACK_DAMAGE = 10;

    /**
     * 玩家攻击范围
     */
    public static final float PLAYER_ATTACK_RANGE = 50;

    /**
     * 玩家攻击冷却（秒）
     */
    public static final float PLAYER_ATTACK_COOLDOWN = 0.5f;

    /**
     * 普通敌人生命值
     */
    public static final float ENEMY_NORMAL_HEALTH = 50;

    /**
     * 精英敌人生命值
     */
    public static final float ENEMY_ELITE_HEALTH = 150;

    /**
     * Boss 生命值
     */
    public static final float ENEMY_BOSS_HEALTH = 500;

    /**
     * 最大敌人数量
     */
    public static final int MAX_ENEMIES = 10;

    /**
     * 敌人生成间隔（秒）
     */
    public static final float ENEMY_SPAWN_INTERVAL = 3.0f;

    /**
     * 游戏区域边界（左）
     */
    public static final float BOUNDARY_LEFT = -600;

    /**
     * 游戏区域边界（右）
     */
    public static final float BOUNDARY_RIGHT = 600;

    /**
     * 游戏区域边界（下）
     */
    public static final float BOUNDARY_BOTTOM = -350;

    /**
     * 游戏区域边界（上）
     */
    public static final float BOUNDARY_TOP = 350;
}
