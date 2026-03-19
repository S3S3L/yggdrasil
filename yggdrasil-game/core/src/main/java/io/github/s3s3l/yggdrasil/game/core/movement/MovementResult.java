package io.github.s3s3l.yggdrasil.game.core.movement;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import lombok.Builder;
import lombok.Getter;

/**
 * 移动结果
 * 包含移动验证的结果和详细信息
 */
@Getter
@Builder
public class MovementResult {

    /**
     * 移动是否合法
     */
    private final boolean valid;

    /**
     * 移动消耗的移动力
     */
    private final int movementCost;

    /**
     * 失败原因
     */
    private final String reason;

    /**
     * 创建成功的移动结果
     *
     * @param movementCost 移动消耗
     * @return 移动结果
     */
    public static MovementResult success(int movementCost) {
        return MovementResult.builder()
                .valid(true)
                .movementCost(movementCost)
                .build();
    }

    /**
     * 创建失败的移动结果
     *
     * @param reason 失败原因
     * @return 移动结果
     */
    public static MovementResult failure(String reason) {
        return MovementResult.builder()
                .valid(false)
                .reason(reason)
                .movementCost(0)
                .build();
    }

    /**
     * 创建失败的移动结果（目标不可通过）
     *
     * @param position 目标位置
     * @return 移动结果
     */
    public static MovementResult impassable(Coordinate position) {
        return failure("Position " + position + " is impassable");
    }

    /**
     * 创建失败的移动结果（移动力不足）
     *
     * @param required 需要的移动力
     * @param current  当前移动力
     * @return 移动结果
     */
    public static MovementResult insufficientMovement(int required, int current) {
        return failure("Insufficient movement: required " + required + ", has " + current);
    }
}
