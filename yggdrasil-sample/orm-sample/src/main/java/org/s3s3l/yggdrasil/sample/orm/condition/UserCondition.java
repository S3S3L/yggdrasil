package org.s3s3l.yggdrasil.sample.orm.condition;

import org.s3s3l.yggdrasil.orm.bind.annotation.Condition;
import org.s3s3l.yggdrasil.orm.bind.annotation.SqlModel;
import org.s3s3l.yggdrasil.sample.orm.dao.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@SqlModel(table = User.class)
public class UserCondition {
    @Condition(forUpdate = true, forDelete = true)
    private String id;
}
