package org.s3s3l.yggdrasil.orm.test;

import java.util.LinkedList;
import java.util.List;

import org.s3s3l.yggdrasil.orm.bind.annotation.Column;
import org.s3s3l.yggdrasil.orm.bind.annotation.Condition;
import org.s3s3l.yggdrasil.orm.bind.annotation.SqlModel;
import org.s3s3l.yggdrasil.orm.bind.express.common.DefaultExpressFactory;
import org.s3s3l.yggdrasil.orm.handler.ArrayTypeHandler;
import org.s3s3l.yggdrasil.orm.validator.DefaultValidatorFactory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BindTest {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @SqlModel(table = "t_user")
    public static class User {
        @Column(isPrimary = true)
        @Condition
        private String id;
        @Column
        private String name;
        @Column
        private short sex;
        @Column(name = "nick_names", typeHandler = ArrayTypeHandler.class)
        private String[] nickNames;
        @Column
        private int age;
    }

    public static void main(String[] args) {
        System.out.println(new DefaultExpressFactory().getDataBindExpress(User.class, new DefaultValidatorFactory())
                .getSelect(User.builder()
                        .id("id")
                        .build())
                .getSql());
        List<User> list = new LinkedList<>();
        list.add(User.builder()
                .id("id")
                .build());
        System.out.println(new DefaultExpressFactory().getDataBindExpress(User.class, new DefaultValidatorFactory())
                .getInsert(list)
                .getSql());
    }
}
