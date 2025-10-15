package io.github.s3s3l.yggdrasil.game.core.basic;

import java.util.Set;

public interface TreeNode<T> {
    
    /**
     * 获取当前对象的父对象
     * 
     * @return
     */
    T parent();

    /**
     * 设置当前对象的父对象
     * 
     * @param parent
     */
    void setParent(T parent);

    /**
     * 获取当前对象的子对象
     * 
     * @return
     */
    Set<T> children();

    /**
     * 添加子对象
     * 
     * @param child
     */
    void addChild(T child);
}
