package io.github.s3s3l.yggdrasil.orm.ds;

import java.sql.SQLException;

import javax.sql.DataSource;

import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;

public interface DatasourceHolder extends Transactable {
    void setDatasource(DataSource datasource, DatabaseType databaseType);

    <T> T useConn(ConnFunc<T> func) throws SQLException;

    DatabaseType getDatabaseType();

}
