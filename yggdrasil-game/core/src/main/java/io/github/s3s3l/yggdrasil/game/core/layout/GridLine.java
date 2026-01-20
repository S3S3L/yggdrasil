package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 网格线类
 * 表示网格线的位置，可以使用数字、名称或跨度表示
 */
public class GridLine {
    /** 网格线编号 */
    private final int line;
    /** 网格线名称 */
    private final String name;
    /** 是否为跨度表示 */
    private final boolean isSpan;

    /**
     * 构造函数，使用数字指定网格线
     * @param line 网格线编号
     */
    public GridLine(int line) {
        this.line = line;
        this.name = null;
        this.isSpan = false;
    }

    /**
     * 构造函数，使用名称指定网格线
     * @param name 网格线名称
     */
    public GridLine(String name) {
        this.line = 0;
        this.name = name;
        this.isSpan = false;
    }

    /**
     * 创建跨度表示的网格线
     * @param count 跨度数量
     * @return 网格线对象
     */
    public static GridLine span(int count) {
        return new GridLine(count, null, true);
    }

    /**
     * 私有构造函数，用于创建跨度表示
     * @param line 网格线编号或跨度数量
     * @param name 网格线名称
     * @param isSpan 是否为跨度
     */
    private GridLine(int line, String name, boolean isSpan) {
        this.line = line;
        this.name = name;
        this.isSpan = isSpan;
    }

    /**
     * 获取网格线编号
     * @return 网格线编号
     */
    public int getLine() {
        return line;
    }

    /**
     * 获取网格线名称
     * @return 网格线名称
     */
    public String getName() {
        return name;
    }

    /**
     * 判断是否为跨度表示
     * @return true表示是跨度，false表示不是
     */
    public boolean isSpan() {
        return isSpan;
    }

    @Override
    public String toString() {
        if (isSpan) {
            return "span " + line;
        }
        if (name != null) {
            return name;
        }
        return String.valueOf(line);
    }
}
