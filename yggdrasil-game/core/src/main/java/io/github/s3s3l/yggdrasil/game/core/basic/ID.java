package io.github.s3s3l.yggdrasil.game.core.basic;

public interface ID {
    
    /**
     * 获取当前对象的唯一标识符
     * 
     * @return
     */
    long getId();

    /**
     * 设置当前对象的唯一标识符
     * 
     * @param id
     */
    void setId(long id);
}
