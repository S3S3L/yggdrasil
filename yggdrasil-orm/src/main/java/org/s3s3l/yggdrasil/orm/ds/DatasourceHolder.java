package org.s3s3l.yggdrasil.orm.ds;

import java.sql.SQLException;

import javax.sql.DataSource;

public interface DatasourceHolder {
    void setDatasource(DataSource datasource);

    <T> T useConn(ConnFunc<T> func) throws SQLException;
}
