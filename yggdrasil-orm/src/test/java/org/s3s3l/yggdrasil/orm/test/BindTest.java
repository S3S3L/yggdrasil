package org.s3s3l.yggdrasil.orm.test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.s3s3l.yggdrasil.orm.bind.annotation.Column;
import org.s3s3l.yggdrasil.orm.bind.annotation.Condition;
import org.s3s3l.yggdrasil.orm.bind.annotation.Limit;
import org.s3s3l.yggdrasil.orm.bind.annotation.Offset;
import org.s3s3l.yggdrasil.orm.bind.annotation.OrderBy;
import org.s3s3l.yggdrasil.orm.bind.annotation.SqlModel;
import org.s3s3l.yggdrasil.orm.bind.annotation.TableDefine;
import org.s3s3l.yggdrasil.orm.bind.express.common.DefaultExpressFactory;
import org.s3s3l.yggdrasil.orm.bind.express.jsqlparser.JSqlParserDataBindExpress;
import org.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import org.s3s3l.yggdrasil.orm.enumerations.ComparePattern;
import org.s3s3l.yggdrasil.orm.handler.ArrayTypeHandler;
import org.s3s3l.yggdrasil.orm.meta.MetaManager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BindTest {

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @TableDefine(table = "t_user")
        public static class User {
                @Column(isPrimary = true)
                @Condition(forDelete = true)
                private String id;
                @Column
                private String name;
                @Column
                private short sex;
                @Column(typeHandler = ArrayTypeHandler.class)
                @Condition(forDelete = true, pattern = ComparePattern.IN)
                private String[] phones;
                @Column
                private int age;
        }

        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @SqlModel(table = User.class)
        public static class UserCondition {
                @Condition(column = "name", forDelete = true, pattern = ComparePattern.IN)
                private List<String> names;
                @Condition(column = "age", pattern = ComparePattern.NOT_LAGER)
                private int maxAge;
                @Condition(column = "age", pattern = ComparePattern.NOT_LESS)
                private int minAge;
                @OrderBy(desc = true)
                private int sort;
                @Offset
                private long offset;
                @Limit
                private long limit;
        }

        public static void main(String[] args) {

                MetaManager metaManager = new MetaManager("org.s3s3l.yggdrasil.orm.test");
                metaManager.refresh();

                System.out.println(new DefaultExpressFactory().getDataBindExpress(User.class, metaManager)
                                .getSelect(User.builder()
                                                .id("id1")
                                                .build())
                                .getSql());
                List<User> list = new LinkedList<>();
                list.add(User.builder()
                                .id("id1")
                                .build());
                System.out.println(new DefaultExpressFactory().getDataBindExpress(User.class, metaManager)
                                .getInsert(list)
                                .getSql());

                list.add(User.builder()
                                .id("id2")
                                .name("name2")
                                .age(23)
                                .phones(new String[] { "phone2.1", "phone2.2" })
                                .build());

                JSqlParserDataBindExpress jSqlParserDataBindExpress = new JSqlParserDataBindExpress(User.class,
                                metaManager);
                SqlStruct insert = jSqlParserDataBindExpress.getInsert(list);

                System.out.println(insert.getSql());

                insert.getParams()
                                .forEach(System.out::println);

                SqlStruct delete = jSqlParserDataBindExpress.getDelete(User.builder()
                                .id("id3")
                                .phones(new String[] { "phone3.1", "phone3.2" })
                                .build());
                System.out.println(delete.getSql());

                delete.getParams()
                                .forEach(System.out::println);

                JSqlParserDataBindExpress conditionExpress = new JSqlParserDataBindExpress(UserCondition.class,
                                metaManager);
                SqlStruct conditionDelete = conditionExpress.getDelete(UserCondition.builder()
                                .names(Arrays.asList("name4.1", "name4.2"))
                                .maxAge(18)
                                .minAge(6)
                                .build());
                System.out.println(conditionDelete.getSql());

                conditionDelete.getParams()
                                .forEach(System.out::println);

                SqlStruct conditionSelect = conditionExpress.getSelect(UserCondition.builder()
                                .names(Arrays.asList("name5.1", "name5.2"))
                                .maxAge(18)
                                .minAge(6)
                                .offset(0)
                                .limit(10)
                                .build());
                System.out.println(conditionSelect.getSql());

                conditionSelect.getParams()
                                .forEach(System.out::println);
        }
}
