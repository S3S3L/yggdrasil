package io.github.s3s3l.yggdrasil.orm.test.proxy;

import java.util.List;

import io.github.s3s3l.yggdrasil.orm.bind.annotation.ExecutorProxy;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.Param;
import io.github.s3s3l.yggdrasil.orm.test.condition.UserCondition;
import io.github.s3s3l.yggdrasil.orm.test.dao.User;

@ExecutorProxy
public interface UserProxy {

    long userCount(UserCondition condition);

    List<User> list(@Param("condition") UserCondition condition);

    User get(@Param("condition") UserCondition condition);

    int addOne(@Param("user") User user);
}
