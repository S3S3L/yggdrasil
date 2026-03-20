package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * 数据变更监听器
 */
public interface DataChangeListener<T> {

    /**
     * 数据变更时调用
     *
     * @param oldValue 旧值
     * @param newValue 新值
     */
    void onDataChanged(T oldValue, T newValue);
}
