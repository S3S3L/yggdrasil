package io.github.s3s3l.yggdrasil.bean;

import java.util.List;

/**
 * <p>
 * </p>
 * ClassName:Node <br>
 * Date: Jan 3, 2018 2:51:55 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class Node implements Cacheable {

    /** 
     * @since JDK 1.8
     */  
    private static final long serialVersionUID = -9035585673470498500L;
    protected String name;
    protected String guid;
    protected List<Node> children;

    protected Node() {
    }

    protected Node(String name, String guid) {
        this.name = name;
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
