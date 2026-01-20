package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 网格区域类
 * 表示命名的网格区域
 */
public class GridArea {
    /** 区域名称 */
    private final String name;

    /**
     * 构造函数
     * @param name 区域名称
     */
    public GridArea(String name) {
        this.name = name;
    }

    /**
     * 获取区域名称
     * @return 区域名称
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
