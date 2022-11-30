package org.s3s3l.yggdrasil.sample.dss.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.s3s3l.yggdrasil.sample.dss.condition.UserCondition;
import org.s3s3l.yggdrasil.sample.dss.dao.User;

public interface UserMapper {
    long userCount(@Param("condition") UserCondition condition);

    List<User> list(@Param("condition") UserCondition condition);

    User get(@Param("condition") UserCondition condition);
}
