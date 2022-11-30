package org.s3s3l.yggdrasil.sample.dss.proxy;

import java.util.List;

import org.s3s3l.yggdrasil.orm.bind.annotation.ExecutorProxy;
import org.s3s3l.yggdrasil.orm.bind.annotation.Param;
import org.s3s3l.yggdrasil.sample.dss.condition.UserCondition;
import org.s3s3l.yggdrasil.sample.dss.dao.User;

@ExecutorProxy
public interface UserProxy {

    long userCount(UserCondition condition);

    List<User> list(@Param("condition") UserCondition condition);

    User get(@Param("condition") UserCondition condition);
}
