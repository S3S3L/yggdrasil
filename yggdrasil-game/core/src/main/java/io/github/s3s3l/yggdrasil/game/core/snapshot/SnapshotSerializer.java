package io.github.s3s3l.yggdrasil.game.core.snapshot;

/**
 * 快照序列化器接口
 * 用于将快照对象序列化和反序列化
 */
public interface SnapshotSerializer {

    /**
     * 将快照对象序列化为字符串
     *
     * @param snapshot 快照对象
     * @return 序列化后的字符串
     */
    String serialize(Snapshot snapshot);

    /**
     * 将字符串反序列化为快照对象
     *
     * @param data 序列化数据
     * @param targetType 目标快照类型
     * @return 快照对象
     * @param <T> 快照类型
     */
    <T extends Snapshot> T deserialize(String data, Class<T> targetType);
}
