package org.s3s3l.yggdrasil.orm.bind.sql;

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
public class DefaultSqlStruct implements SqlStruct {

    private String sql = StringUtils.EMPTY_STRING;
    private List<Object> params = new ArrayList<>();

    public void addParam(Object... param) {
        this.params.addAll(Arrays.asList(param));
    }

    public DefaultSqlStruct addParams(List<Object> params) {
        this.params.addAll(params);
        return this;
    }

    public DefaultSqlStruct appendSql(String sql) {
        this.sql = new StringBuilder(this.sql == null ? StringUtils.EMPTY_STRING : this.sql).append(sql)
                .toString();
        return this;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public List<Object> getParams() {
        return params;
    }
}
