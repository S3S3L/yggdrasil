package io.github.s3s3l.yggdrasil.game.core.config;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

/**
 * 数据行
 * 数据表中的单行数据基类
 */
public abstract class DataRow implements ID {

    private long numericId;

    @Override
    public long getId() {
        return numericId;
    }

    @Override
    public void setId(long id) {
        this.numericId = id;
    }

    /**
     * 验证数据
     *
     * @return 是否有效
     */
    public boolean validate() {
        return numericId > 0;
    }

    /**
     * 数据初始化回调
     */
    public void onInit() {
        // 子类可以实现此方法进行初始化
    }
}
