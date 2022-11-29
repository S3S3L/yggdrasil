package org.s3s3l.yggdrasil.sample.orm.proxy;

import java.util.List;

import org.s3s3l.yggdrasil.orm.bind.annotation.ExecutorProxy;
import org.s3s3l.yggdrasil.sample.orm.condition.UserCondition;
import org.s3s3l.yggdrasil.sample.orm.dao.User;

@ExecutorProxy
public interface UserProxy {
    
    long userCount(UserCondition condition);

    List<User> list(UserCondition condition);

    User get(UserCondition condition);
}
