package io.github.s3s3l.yggdrasil.game.core.action;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;

/**
 * 动作验证器接口
 * 验证动作是否可以执行
 */
public interface ActionValidator {

    /**
     * 验证动作是否可以执行
     *
     * @param action 动作
     * @param unit   执行动作的单位
     * @param target 目标
     * @return 验证结果
     */
    ValidationResult validate(Action action, Unit unit, Object target);

    /**
     * 验证结果
     */
    @lombok.Getter
    @lombok.RequiredArgsConstructor
    class ValidationResult {
        private final boolean valid;
        private final String reason;

        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult invalid(String reason) {
            return new ValidationResult(false, reason);
        }
    }
}
