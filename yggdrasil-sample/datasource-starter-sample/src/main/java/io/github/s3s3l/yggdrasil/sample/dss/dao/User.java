package io.github.s3s3l.yggdrasil.sample.dss.dao;

import java.sql.JDBCType;

import io.github.s3s3l.yggdrasil.orm.bind.annotation.Column;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.TableDefine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableDefine(table = "t_user")
public class User {
    @Column(dbType = @DatabaseType(type = JDBCType.VARCHAR, args = { "64" }, primary = true))
    private String id;
    @Column(dbType = @DatabaseType(type = JDBCType.VARCHAR, args = { "32" }))
    private String realName;
    @Column(dbType = @DatabaseType(type = JDBCType.VARCHAR, args = { "32" }))
    private String phone;
    @Column(dbType = @DatabaseType(type = JDBCType.VARCHAR, args = { "32" }, notNull = true))
    private String username;
    @Column(dbType = @DatabaseType(type = JDBCType.VARCHAR, args = { "64" }, notNull = true))
    private String password;
    @Column(dbType = @DatabaseType(type = JDBCType.INTEGER))
    private int age;
}
