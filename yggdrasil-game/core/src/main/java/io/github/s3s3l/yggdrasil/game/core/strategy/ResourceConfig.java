package io.github.s3s3l.yggdrasil.game.core.strategy;

/**
 * 资源配置
 */
public class ResourceConfig {

    /**
     * 默认最大容量
     */
    private int defaultMaxCapacity;

    /**
     * 是否允许负资源
     */
    private boolean allowNegativeResources;

    /**
     * 是否自动执行生产
     */
    private boolean autoProduction;

    /**
     * 是否自动执行消耗
     */
    private boolean autoConsumption;

    public ResourceConfig() {
        this.defaultMaxCapacity = 1000;
        this.allowNegativeResources = false;
        this.autoProduction = true;
        this.autoConsumption = true;
    }

    public int getDefaultMaxCapacity() {
        return defaultMaxCapacity;
    }

    public void setDefaultMaxCapacity(int defaultMaxCapacity) {
        this.defaultMaxCapacity = defaultMaxCapacity;
    }

    public boolean isAllowNegativeResources() {
        return allowNegativeResources;
    }

    public void setAllowNegativeResources(boolean allowNegativeResources) {
        this.allowNegativeResources = allowNegativeResources;
    }

    public boolean isAutoProduction() {
        return autoProduction;
    }

    public void setAutoProduction(boolean autoProduction) {
        this.autoProduction = autoProduction;
    }

    public boolean isAutoConsumption() {
        return autoConsumption;
    }

    public void setAutoConsumption(boolean autoConsumption) {
        this.autoConsumption = autoConsumption;
    }
}
