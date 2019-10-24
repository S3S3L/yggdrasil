package org.s3s3l.yggdrasil.orm.bind;

/**
 * 
 * <p>
 * </p>
 * ClassName: Table <br>
 * date: Sep 20, 2019 11:29:48 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class Table {

    private String name;
    private String alias;

    public Table() {

    }

    public Table(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
