package io.github.s3s3l.yggdrasil.game.core.snapshot;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 状态快照，用于保存游戏对象状态的序列化表示
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StateSnapshot {

    /**
     * 状态数据（JSON 格式）
     */
    private JsonNode data;

    /**
     * 状态类型
     */
    private Class<?> type;
}
