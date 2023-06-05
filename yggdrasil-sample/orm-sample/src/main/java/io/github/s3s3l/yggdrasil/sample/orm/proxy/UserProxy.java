package io.github.s3s3l.yggdrasil.sample.orm.proxy;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import io.github.s3s3l.yggdrasil.orm.bind.annotation.ExecutorProxy;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.Param;
import io.github.s3s3l.yggdrasil.sample.orm.condition.UserCondition;
import io.github.s3s3l.yggdrasil.sample.orm.dao.User;

@ExecutorProxy
public interface UserProxy {

    long userCount(UserCondition condition);

    List<User> list(@Param("condition") UserCondition condition);

    User get(@Param("condition") UserCondition condition);

    int addOne(@Param("user") User user);

    public static void main(String[] args) throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "psql", "Psql@1122");
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet columns = metaData.getColumns(null, "public", "t_user", null);

        while (columns.next()) {
            System.out.println("==============================");
            for (int i = 1; i <= columns.getMetaData()
                    .getColumnCount(); i++) {
                System.out.print(columns.getMetaData()
                        .getColumnLabel(i));
                System.out.print(" ");
                System.out.println(columns.getObject(i));
            }
        }
    }
}
