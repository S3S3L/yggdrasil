package io.github.s3s3l.yggdrasil.game.core.cover;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import lombok.Getter;

/**
 * 掩体值
 * 表示单位在特定位置获得的掩体效果
 */
@Getter
public class CoverValue {

    /**
     * 掩体类型
     */
    private final CoverType coverType;

    /**
     * 掩体方向（相对于单位的角度）
     * 0-360 度
     */
    private final int direction;

    /**
     * 掩体来源坐标（如果有）
     */
    private final Coordinate coverSource;

    /**
     * 创建掩体值
     *
     * @param coverType  掩体类型
     * @param direction  掩体方向
     * @param coverSource 掩体来源坐标
     */
    public CoverValue(CoverType coverType, int direction, Coordinate coverSource) {
        this.coverType = coverType;
        this.direction = direction;
        this.coverSource = coverSource;
    }

    /**
     * 创建掩体值（无方向）
     *
     * @param coverType 掩体类型
     */
    public CoverValue(CoverType coverType) {
        this(coverType, -1, null);
    }

    /**
     * 创建掩体值（带来源）
     *
     * @param coverType   掩体类型
     * @param coverSource 掩体来源坐标
     */
    public CoverValue(CoverType coverType, Coordinate coverSource) {
        this(coverType, -1, coverSource);
    }

    /**
     * 获取伤害减免百分比
     *
     * @return 伤害减免（0-100）
     */
    public int getDamageReduction() {
        return coverType.getDamageReduction();
    }

    /**
     * 获取防御加成
     *
     * @return 防御加成
     */
    public int getDefenseBonus() {
        return coverType.getDefenseBonus();
    }

    /**
     * 计算减免后的伤害
     *
     * @param damage 原始伤害
     * @return 减免后的伤害
     */
    public int applyDamageReduction(int damage) {
        return coverType.applyDamageReduction(damage);
    }

    /**
     * 检查是否有掩体
     *
     * @return true 如果有掩体
     */
    public boolean hasCover() {
        return coverType != CoverType.NONE;
    }

    /**
     * 检查掩体是否来自特定方向
     *
     * @param attackDirection 攻击方向
     * @return true 如果掩体来自该方向
     */
    public boolean isCoverFromDirection(int attackDirection) {
        if (direction < 0) {
            return hasCover(); // 无方向掩体对所有方向有效
        }

        // 允许 45 度误差
        int diff = Math.abs(attackDirection - direction);
        return diff <= 45 || diff >= 315;
    }

    /**
     * 创建无掩体值
     *
     * @return 无掩体值
     */
    public static CoverValue none() {
        return new CoverValue(CoverType.NONE);
    }

    /**
     * 创建轻掩体值
     *
     * @return 轻掩体值
     */
    public static CoverValue light() {
        return new CoverValue(CoverType.LIGHT);
    }

    /**
     * 创建半掩体值
     *
     * @return 半掩体值
     */
    public static CoverValue half() {
        return new CoverValue(CoverType.HALF);
    }

    /**
     * 创建全掩体值
     *
     * @return 全掩体值
     */
    public static CoverValue full() {
        return new CoverValue(CoverType.FULL);
    }
}
