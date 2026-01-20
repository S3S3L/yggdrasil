package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 长度单位类
 * 表示带单位的长度值，类似CSS中的长度单位
 */
public class LengthUnit {
    /** 数值 */
    private final float value;
    /** 单位类型 */
    private final Unit unit;

    /**
     * 单位类型枚举
     */
    public enum Unit {
        /** 像素 */
        PX,
        /** 百分比 */
        PERCENT,
        /** 自动 */
        AUTO,
        /** 分数单位（用于网格布局） */
        FR,
        /** 视口宽度 */
        VW,
        /** 视口高度 */
        VH
    }

    /**
     * 构造函数
     * @param value 数值
     * @param unit 单位类型
     */
    public LengthUnit(float value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    /**
     * 创建像素单位
     * @param value 像素值
     * @return 长度单位对象
     */
    public static LengthUnit px(float value) {
        return new LengthUnit(value, Unit.PX);
    }

    /**
     * 创建百分比单位
     * @param value 百分比值
     * @return 长度单位对象
     */
    public static LengthUnit percent(float value) {
        return new LengthUnit(value, Unit.PERCENT);
    }

    /**
     * 创建自动单位
     * @return 长度单位对象
     */
    public static LengthUnit auto() {
        return new LengthUnit(0, Unit.AUTO);
    }

    /**
     * 创建分数单位（用于网格）
     * @param value 分数值
     * @return 长度单位对象
     */
    public static LengthUnit fr(float value) {
        return new LengthUnit(value, Unit.FR);
    }

    /**
     * 获取数值
     * @return 数值
     */
    public float getValue() {
        return value;
    }

    /**
     * 获取单位类型
     * @return 单位类型
     */
    public Unit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        if (unit == Unit.AUTO) {
            return "auto";
        }
        return value + unit.name().toLowerCase();
    }
}
