package io.github.s3s3l.yggdrasil.sample.dss.proxy;

import java.util.List;

import io.github.s3s3l.yggdrasil.orm.bind.annotation.ExecutorProxy;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.Param;
import io.github.s3s3l.yggdrasil.sample.dss.condition.UserCondition;
import io.github.s3s3l.yggdrasil.sample.dss.dao.User;

@ExecutorProxy
public interface UserProxy {

    long userCount(UserCondition condition);

    List<User> list(@Param("condition") UserCondition condition);

    User get(@Param("condition") UserCondition condition);
}
