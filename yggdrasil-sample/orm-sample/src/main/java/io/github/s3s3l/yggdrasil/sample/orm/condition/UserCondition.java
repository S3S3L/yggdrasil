package io.github.s3s3l.yggdrasil.sample.orm.condition;

import io.github.s3s3l.yggdrasil.orm.bind.annotation.Condition;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.SqlModel;
import io.github.s3s3l.yggdrasil.orm.pagin.ConditionForPagination;
import io.github.s3s3l.yggdrasil.sample.orm.dao.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SqlModel(table = User.class)
public class UserCondition extends ConditionForPagination {
    @Condition(forUpdate = true, forDelete = true)
    private String id;
    private String[] ids;
}
