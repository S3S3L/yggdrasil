package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * 可观察属性
 * 支持数据变更监听
 *
 * @param <T> 属性类型
 */
public class ObservableProperty<T> {

    private T value;
    private final List<DataChangeListener<T>> listeners;

    public ObservableProperty() {
        this.listeners = new ArrayList<>();
    }

    public ObservableProperty(T value) {
        this.value = value;
        this.listeners = new ArrayList<>();
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        T oldValue = this.value;
        this.value = value;
        notifyListeners(oldValue, value);
    }

    /**
     * 添加变更监听器
     *
     * @param listener 监听器
     */
    public void addChangeListener(DataChangeListener<T> listener) {
        listeners.add(listener);
    }

    /**
     * 移除变更监听器
     *
     * @param listener 监听器
     */
    public void removeChangeListener(DataChangeListener<T> listener) {
        listeners.remove(listener);
    }

    /**
     * 通知所有监听器
     *
     * @param oldValue 旧值
     * @param newValue 新值
     */
    protected void notifyListeners(T oldValue, T newValue) {
        for (DataChangeListener<T> listener : listeners) {
            try {
                listener.onDataChanged(oldValue, newValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清除所有监听器
     */
    public void clearListeners() {
        listeners.clear();
    }

    /**
     * 获取监听器数量
     *
     * @return 监听器数量
     */
    public int getListenerCount() {
        return listeners.size();
    }
}
