package io.github.s3s3l.yggdrasil.game.core.flow;

/**
 * 游戏状态枚举
 * 定义游戏的各种状态
 */
public enum GameState {
    /**
     * 初始状态
     */
    INITIALIZING,

    /**
     * 主菜单
     */
    MAIN_MENU,

    /**
     * 游戏中（活跃状态）
     */
    PLAYING,

    /**
     * 游戏暂停
     */
    PAUSED,

    /**
     * 游戏结束
     */
    GAME_OVER,

    /**
     * 胜利
     */
    VICTORY,

    /**
     * 失败
     */
    DEFEAT,

    /**
     * 加载画面
     */
    LOADING,

    /**
     * 过场动画
     */
    CUTSCENE,

    /**
     * 设置菜单
     */
    SETTINGS,

    /**
     * 存档/读档菜单
     */
    SAVE_LOAD;
}
