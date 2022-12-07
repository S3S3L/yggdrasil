package io.github.s3s3l.yggdrasil.orm.ds;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnFunc<R> {
    R execute(Connection conn) throws SQLException, SecurityException;
}
