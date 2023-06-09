package io.github.s3s3l.yggdrasil.orm.test;

import java.sql.JDBCType;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import io.github.s3s3l.yggdrasil.orm.bind.annotation.Column;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.Condition;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.GroupBy;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.OrderBy;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.SqlModel;
import io.github.s3s3l.yggdrasil.orm.bind.annotation.TableDefine;
import io.github.s3s3l.yggdrasil.orm.bind.express.DataBindExpress;
import io.github.s3s3l.yggdrasil.orm.bind.express.ExpressBuilderType;
import io.github.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import io.github.s3s3l.yggdrasil.orm.enumerations.ComparePattern;
import io.github.s3s3l.yggdrasil.orm.factory.DataBindExpressFactory;
import io.github.s3s3l.yggdrasil.orm.factory.DbTypeHandlerFactory;
import io.github.s3s3l.yggdrasil.orm.handler.ArrayTypeHandler;
import io.github.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManagerConfig;
import io.github.s3s3l.yggdrasil.orm.meta.TableMeta;
import io.github.s3s3l.yggdrasil.orm.pagin.ConditionForPagination;
import io.github.s3s3l.yggdrasil.orm.validator.PositiveNumberValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class BindTest {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @TableDefine(table = "t_user")
    public static class User {
        @Column(dbType = @DatabaseType(type = JDBCType.VARCHAR, args = { "64" }, primary = true))
        @Condition(forDelete = true)
        private String id;
        @Column(dbType = @DatabaseType(type = JDBCType.VARCHAR, args = { "32" }, notNull = true))
        private String name;
        @Column(validator = PositiveNumberValidator.class, dbType = @DatabaseType(type = JDBCType.SMALLINT))
        private short sex;
        @Column(typeHandler = ArrayTypeHandler.class, dbType = @DatabaseType(type = JDBCType.VARCHAR, array = true))
        @Condition(forDelete = true, pattern = ComparePattern.IN)
        private String[] phones;
        @Column(validator = PositiveNumberValidator.class, dbType = @DatabaseType(type = JDBCType.INTEGER))
        private int age;
    }

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @SqlModel(table = User.class)
    public static class UserCondition extends ConditionForPagination {
        @GroupBy
        private String name;
        @Condition(column = "name", forDelete = true, forUpdate = true, pattern = ComparePattern.IN)
        private List<String> names;
        @Condition(column = "age", pattern = ComparePattern.NOT_LAGER)
        private int maxAge;
        @Condition(column = "age", pattern = ComparePattern.NOT_LESS)
        private int minAge;
        @OrderBy(desc = true)
        private int sort;
    }

    public static void main(String[] args) {
        MetaManager metaManager = new MetaManager(MetaManagerConfig.defaultBuilder()
                .tableDefinePackages(new String[] { "io.github.s3s3l.yggdrasil.orm.test" })
                .build());
        io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType databaseType = io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType.POSTGRESQL;

        DataBindExpressFactory dataBindExpressFactory = new DataBindExpressFactory();

        dataBindExpressFactory.initDefault(metaManager, DbTypeHandlerFactory.DEFAULT);
        DataBindExpress jsqlDataBindExpress = dataBindExpressFactory.getInstance(databaseType,
                ExpressBuilderType.JSQLPARSER);
        DataBindExpress defaulDataBindExpress = dataBindExpressFactory.getInstance(databaseType,
                ExpressBuilderType.DEFAULT);

        System.out.println("[DefaultDataBindExpress]" + defaulDataBindExpress.getSelect(User.builder()
                .id("id1")
                .build())
                .getSql());
        System.out.println("[JSqlParserDataBindExpress]" + jsqlDataBindExpress.getSelect(User.builder()
                .id("id1")
                .build())
                .getSql());
        List<User> list = new LinkedList<>();
        list.add(User.builder()
                .id("id1")
                .build());
        System.out.println("[DefaultDataBindExpress]" + defaulDataBindExpress.getInsert(list)
                .getSql());
        System.out.println("[JSqlParserDataBindExpress]" + defaulDataBindExpress.getInsert(list)
                .getSql());

        list.add(User.builder()
                .id("id2")
                .name("name2")
                .age(23)
                .phones(new String[] { "phone2.1", "phone2.2" })
                .build());
        System.out.println("[DefaultDataBindExpress]" + defaulDataBindExpress.getInsert(list)
                .getSql());

        SqlStruct insert = jsqlDataBindExpress.getInsert(list);

        System.out.println("[JSqlParserDataBindExpress]" + insert.getSql());

        insert.getParams()
                .forEach(System.out::println);

        SqlStruct delete = jsqlDataBindExpress.getDelete(User.builder()
                .id("id3")
                .phones(new String[] { "phone3.1", "phone3.2" })
                .build());
        System.out.println(delete.getSql());

        delete.getParams()
                .forEach(System.out::println);

        SqlStruct conditionDelete = jsqlDataBindExpress.getDelete(UserCondition.builder()
                .names(Arrays.asList("name4.1", "name4.2"))
                .maxAge(18)
                .minAge(6)
                .build());
        System.out.println(conditionDelete.getSql());

        conditionDelete.getParams()
                .forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>> select");
        UserCondition selectCondition = UserCondition.builder()
                .names(Arrays.asList("name5.1", "name5.2"))
                .maxAge(18)
                .minAge(6)
                .offset(0L)
                .limit(10L)
                .build();
        System.out.println(">>>>>>>>>>>>>>>>> JSqlParserDataBindExpress");
        SqlStruct conditionSelect = jsqlDataBindExpress.getSelect(selectCondition);
        System.out.println(conditionSelect.getSql());

        conditionSelect.getParams()
                .forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>> DefaultDataBindExpress");
        conditionSelect = defaulDataBindExpress.getSelect(selectCondition);
        System.out.println(conditionSelect.getSql());

        conditionSelect.getParams()
                .forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>> selectCount");

        System.out.println(">>>>>>>>>>>>>>>>> JSqlParserDataBindExpress");
        conditionSelect = jsqlDataBindExpress.getSelectCount(selectCondition);
        System.out.println(conditionSelect.getSql());
        conditionSelect.getParams()
                .forEach(System.out::println);
        System.out.println(">>>>>>>>>>>>>>>>> DefaultDataBindExpress");
        conditionSelect = defaulDataBindExpress.getSelectCount(selectCondition);
        System.out.println(conditionSelect.getSql());
        conditionSelect.getParams()
                .forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>> JSqlParserDataBindExpress");
        SqlStruct conditionUpdate = jsqlDataBindExpress.getUpdate(User.builder()
                .id("id3")
                .phones(new String[] { "phone3.1", "phone3.2" })
                .build(),
                UserCondition.builder()
                        .name("newName")
                        .names(Arrays.asList("name6.1", "name6.2"))
                        .maxAge(18)
                        .minAge(6)
                        .offset(0L)
                        .limit(10L)
                        .build());
        System.out.println(conditionUpdate.getSql());
        conditionUpdate.getParams()
                .forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>> DefaultDataBindExpress");
        conditionUpdate = defaulDataBindExpress.getUpdate(User.builder()
                .id("id3")
                .phones(new String[] { "phone3.1", "phone3.2" })
                .build(),
                UserCondition.builder()
                        .name("newName")
                        .names(Arrays.asList("name6.1", "name6.2"))
                        .maxAge(18)
                        .minAge(6)
                        .offset(0L)
                        .limit(10L)
                        .build());
        System.out.println(conditionUpdate.getSql());

        conditionUpdate.getParams()
                .forEach(System.out::println);

        System.out.println(">>>>>>>>>>>>>>>>> DefaultDataBindExpress");
        System.out.println(defaulDataBindExpress.getCreate(User.class, false)
                .getSql());
        System.out.println(">>>>>>>>>>>>>>>>> JSqlParserDataBindExpress");
        System.out.println(jsqlDataBindExpress.getCreate(User.class, false)
                .getSql());

        TableMeta userTable = metaManager.getTable(User.class);
        ColumnMeta columnMeta = userTable.getColumns()
                .get(0);
        // columnMeta.getDbType()
        // .setNotNull(true);
        columnMeta.setTableAlias(userTable.getName());
        System.out.println(">>>>>>>>>>>>>>>>> DefaultDataBindExpress");
        System.out.println(defaulDataBindExpress.getColumnAdd(columnMeta)
                .getSql());
        System.out.println(">>>>>>>>>>>>>>>>> JSqlParserDataBindExpress");
        System.out.println(jsqlDataBindExpress.getColumnAdd(columnMeta)
                .getSql());
        System.out.println(">>>>>>>>>>>>>>>>> DefaultDataBindExpress");
        System.out.println(defaulDataBindExpress.getColumnDrop(columnMeta)
                .getSql());
        System.out.println(">>>>>>>>>>>>>>>>> JSqlParserDataBindExpress");
        System.out.println(jsqlDataBindExpress.getColumnDrop(columnMeta)
                .getSql());
        System.out.println(">>>>>>>>>>>>>>>>> DefaultDataBindExpress");
        System.out.println(defaulDataBindExpress.getColumnAlter(columnMeta)
                .getSql());
        System.out.println(">>>>>>>>>>>>>>>>> JSqlParserDataBindExpress");
        System.out.println(jsqlDataBindExpress.getColumnAlter(columnMeta)
                .getSql());
    }
}
