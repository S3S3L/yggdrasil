package org.s3s3l.yggdrasil.starter.datasource.feature;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.s3s3l.yggdrasil.orm.pool.ConnManager;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class SpringConnManager implements ConnManager {

    @Override
    public Connection getConn(DataSource dataSource) throws SQLException {
        return DataSourceUtils.getConnection(dataSource);
    }

    @Override
    public void relaseConn(Connection conn, DataSource dataSource) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
    
}
