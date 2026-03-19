package io.github.s3s3l.yggdrasil.game.core.strategy;

/**
 * 资源消耗接口
 */
public interface ResourceConsumption {

    /**
     * 计算消耗量
     *
     * @param manager 资源管理器
     * @return 消耗量
     */
    int calculateConsumption(ResourceManager manager);

    /**
     * 获取描述
     *
     * @return 描述
     */
    String getDescription();
}
