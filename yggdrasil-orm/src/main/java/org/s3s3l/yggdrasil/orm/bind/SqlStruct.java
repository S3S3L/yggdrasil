package org.s3s3l.yggdrasil.orm.bind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.s3s3l.yggdrasil.utils.common.StringUtils;

/**
 * 
 * <p>
 * </p>
 * ClassName: SqlStruct <br>
 * date: Sep 19, 2019 5:52:22 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class SqlStruct {

    private String sql = StringUtils.EMPTY_STRING;
    private List<Object> params = new ArrayList<>();

    public void addParam(Object... param) {
        this.params.addAll(Arrays.asList(param));
    }

    public SqlStruct addParams(List<Object> params) {
        this.params.addAll(params);
        return this;
    }

    public SqlStruct appendSql(String sql) {
        this.sql = new StringBuilder(this.sql == null ? StringUtils.EMPTY_STRING : this.sql).append(sql)
                .toString();
        return this;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

    public List<Object> getParams() {
        return params;
    }
}
