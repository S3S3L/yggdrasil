package org.s3s3l.yggdrasil.orm.ds;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.s3s3l.yggdrasil.orm.pool.ConnManager;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
public class DefaultDatasourceHolder implements DatasourceHolder {
    private final ConnManager connFactory;

    private DataSource datasource;

    @Override
    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public <T> T useConn(ConnFunc<T> func) throws SQLException {
        Connection conn = connFactory.getConn(datasource);
        try {
            return func.execute(conn);
        } finally {
            connFactory.relaseConn(conn, datasource);
        }
    }
    
}
