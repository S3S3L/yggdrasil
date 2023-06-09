package io.github.s3s3l.yggdrasil.orm.postgresql.test.dao;

import java.sql.JDBCType;

import io.github.s3s3l.yggdrasil.orm.bind.annotation.Column;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.TableDefine;
import io.github.s3s3l.yggdrasil.orm.handler.ArrayTypeHandler;
import io.github.s3s3l.yggdrasil.orm.test.dao.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableDefine(table = User.TABLE_NAME)
public class PgUser extends User {
    
    @Column(dbType = @DatabaseType(type = JDBCType.VARCHAR, array = true), typeHandler = ArrayTypeHandler.class)
    private String[] remarks;
}
