package io.github.s3s3l.yggdrasil.orm.ds;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.pool.ConnManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
public class DefaultDatasourceHolder implements DatasourceHolder {
    private final ConnManager connManager;
    private final ThreadLocal<Connection> currentConn = new ThreadLocal<>();
    private final ThreadLocal<Boolean> isTransactional = new ThreadLocal<>();

    private DataSource datasource;
    @Getter
    private DatabaseType databaseType;

    @Override
    public void setDatasource(DataSource datasource, DatabaseType databaseType) {
        this.datasource = datasource;
        this.databaseType = databaseType;
    }

    @Override
    public <T> T useConn(ConnFunc<T> func) throws SQLException {
        boolean isTrans = Boolean.TRUE.equals(isTransactional.get()) && currentConn.get() != null;
        Connection conn = isTrans ? currentConn.get() : connManager.getConn(datasource);
        try {
            return func.execute(conn);
        } finally {
            if (!isTrans) {
                connManager.relaseConn(conn, datasource);
            }
        }
    }

    @Override
    public void transactional() throws SQLException {
        Connection conn = connManager.getConn(datasource);
        conn.setAutoCommit(false);
        currentConn.set(conn);
        isTransactional.set(true);
    }

    @Override
    public void transactionalCommit() throws SQLException {
        Connection conn = currentConn.get();
        conn.commit();
        conn.setAutoCommit(true);
        conn.close();
        currentConn.remove();
        isTransactional.set(false);
    }

    @Override
    public void rollback() throws SQLException {
        Connection conn = currentConn.get();
        conn.rollback();
        conn.setAutoCommit(true);
        conn.close();
        currentConn.remove();
        isTransactional.set(false);
    }

}
