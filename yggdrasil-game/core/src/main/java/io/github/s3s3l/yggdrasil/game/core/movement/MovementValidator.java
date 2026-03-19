package io.github.s3s3l.yggdrasil.game.core.movement;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;

import java.util.List;

/**
 * 移动验证器接口
 * 验证单位移动是否合法
 */
public interface MovementValidator {

    /**
     * 验证移动是否合法
     *
     * @param unit       移动的单位
     * @param from       起始位置
     * @param to         目标位置
     * @param movement   剩余移动力
     * @return 移动验证结果
     */
    MovementResult validate(Unit unit, Coordinate from, Coordinate to, int movement);

    /**
     * 验证路径是否合法
     *
     * @param unit  移动的单位
     * @param path  移动路径
     * @return true 如果路径合法
     */
    boolean validatePath(Unit unit, List<Coordinate> path);

    /**
     * 获取可移动的位置列表
     *
     * @param unit     移动的单位
     * @param position 当前位置
     * @return 可移动位置列表
     */
    List<Coordinate> getReachablePositions(Unit unit, Coordinate position);
}
