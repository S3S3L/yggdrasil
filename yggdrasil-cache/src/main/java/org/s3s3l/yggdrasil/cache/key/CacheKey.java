package org.s3s3l.yggdrasil.cache.key;

import java.io.Serializable;

/**
 * <p>
 * </p> 
 * ClassName:CacheKey <br> 
 * Date:     Apr 26, 2018 10:16:11 AM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public class CacheKey implements Serializable {

    /** 
     * @since JDK 1.8
     */  
    private static final long serialVersionUID = 5171909332254576298L;
    private String type;
    private String method;
    private Object[] params;

    public String getType() {
        return type;
    }

    public CacheKey setType(String type) {
        this.type = type;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public CacheKey setMethod(String method) {
        this.method = method;
        return this;
    }

    public Object[] getParams() {
        return params;
    }

    public CacheKey setParams(Object[] params) {
        this.params = params;
        return this;
    }
}
  