package io.github.s3s3l.yggdrasil.orm.postgresql.test.condition;

import io.github.s3s3l.yggdrasil.orm.bind.annotation.SqlModel;
import io.github.s3s3l.yggdrasil.orm.postgresql.test.dao.PgUser;
import io.github.s3s3l.yggdrasil.orm.test.condition.UserCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@SqlModel(table = PgUser.class)
@EqualsAndHashCode(callSuper = true)
public class PgUserCondition extends UserCondition {
    
}
