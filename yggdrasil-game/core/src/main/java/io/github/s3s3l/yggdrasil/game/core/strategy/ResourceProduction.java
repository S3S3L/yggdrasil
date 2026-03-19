package io.github.s3s3l.yggdrasil.game.core.strategy;

/**
 * 资源生产接口
 */
public interface ResourceProduction {

    /**
     * 计算生产量
     *
     * @param manager 资源管理器
     * @return 生产量
     */
    int calculateProduction(ResourceManager manager);

    /**
     * 获取描述
     *
     * @return 描述
     */
    String getDescription();
}
