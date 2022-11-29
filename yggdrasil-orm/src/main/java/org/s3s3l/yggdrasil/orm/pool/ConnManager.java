package org.s3s3l.yggdrasil.orm.pool;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public interface ConnManager {
    public static final ConnManager DEFAULT = new ConnManager() {

        @Override
        public Connection getConn(DataSource dataSource) throws SQLException {
            return dataSource.getConnection();
        }

        @Override
        public void relaseConn(Connection conn, DataSource dataSource) throws SQLException {
            conn.close();
        }

    };

    Connection getConn(DataSource dataSource) throws SQLException;

    void relaseConn(Connection conn, DataSource dataSource) throws SQLException;
}
