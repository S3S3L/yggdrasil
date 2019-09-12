package org.s3s3l.yggdrasil.bean.condition;

import org.s3s3l.yggdrasil.bean.Cacheable;

/**
 * <p>
 * </p>
 * ClassName:Choosable <br>
 * Date: Oct 30, 2017 4:42:52 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class Choosable<T> implements Cacheable {

    /** 
     * @since JDK 1.8
     */  
    private static final long serialVersionUID = 4344260756054720751L;
    
    private boolean enable;
    private T data;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
