# ORM

这是一个支持通过注解定义数据对象，并直接通过对象进行简单数据操作的ORM框架。
对于复杂数据操作，额外提供了`ExecutorProxy`来进行类似IBATIS的操作

## 数据对象定义

### 表对象定义

``` java
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableDefine(table = "t_user")
public static class User {
    @Column(dbType = @DatabaseType(type = "varchar", args = { "64" }, primary = true))
    private String id;
    @Column(dbType = @DatabaseType(type = "varchar", args = { "32" }))
    private String realName;
    @Column(dbType = @DatabaseType(type = "varchar", args = { "32" }))
    private String phone;
    @Column(dbType = @DatabaseType(type = "varchar", args = { "32" }, notNull = true))
    private String username;
    @Column(dbType = @DatabaseType(type = "varchar", args = { "64" }, notNull = true))
    private String password;
    @Column(validator = PositiveNumberValidator.class, dbType = @DatabaseType(type = "int"))
    private short sex;
    @Column(typeHandler = ArrayTypeHandler.class, dbType = @DatabaseType(type = "varchar[]"))
    private String[] phones;
    @Column(validator = PositiveNumberValidator.class, dbType = @DatabaseType(type = "int"))
    private int age;
}
```

### 查询条件对象定义

``` java
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@SqlModel(table = User.class)
public static class UserCondition extends ConditionForPagination {
    @Condition(forUpdate = true, forDelete = true)
    private String id;
    private String[] ids;
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
```

## 初始化组件

``` java
// 任意方式初始化DataSource
DataSource datasource = ...;

// 初始化MetaManager
// 初始化过程中会扫描响应的包来初始化数据对象定义
MetaManager metaManager = new MetaManager(MetaManagerConfig.builder()
                .tableDefinePackages(new String[] { "org.s3s3l.yggdrasil.sample.orm" })
                .proxyDefinePackages(new String[] { "org.s3s3l.yggdrasil.sample.orm.proxy" })
                .proxyConfigLocations(new String[] { "proxy" })
                .build());
// 初始化DataBindExpress
// 默认提供两种实现
// 基于JSQLParser进行sql拼接
DataBindExpress jsqlDataBindExpress = new JSqlParserDataBindExpress(metaManager);
// 直接通过字符串拼接进行sql拼接
DataBindExpress defaulDataBindExpress = new DefaultDataBindExpress(metaManager);

// 初始化SqlExecutor
SqlExecutor sqlExecutor = DefaultSqlExecutor.builder()
            // ConnManager用于管理接连，使用spring事务的情况下，请使用SpringConnManager
            .datasourceHolder(new DefaultDatasourceHolder(
                    ConnManager.DEFAULT, datasource))
            .dataBindExpress(defaulDataBindExpress)
            // .dataBindExpress(jsqlDataBindExpress)
            .metaManager(metaManager)
            // freemarker用于复杂sql的模板引擎
            .freeMarkerHelper(new FreeMarkerHelper().config(config -> config
                    .setObjectWrapper(new SqlObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS))))
            .build();
```

## 操作

### 建表

``` java
sqlExecutor.create(User.class, false);
```

### 插入数据

``` java
String id = StringUtils.getUUIDNoLine();
String id2 = StringUtils.getUUIDNoLine();
sqlExecutor.insert(Arrays.asList(
        User.builder()
                .id(id)
                .username("username1")
                .password("pwd1")
                .realName("realName1")
                .age(18)
                .build(),
        User.builder()
                .id(id2)
                .username("username2")
                .password("pwd2")
                .realName("realName2")
                .age(22)
                .build()));
```

### 查询数据

``` java

log.info(">>>>>>>>>>>>>>>>>> select one");
System.out.println(sqlExecutor.selectOne(UserCondition.builder().id(id).build(), User.class));
log.info(">>>>>>>>>>>>>>>>>> select all");
sqlExecutor.select(UserCondition.builder().build(), User.class).forEach(System.out::println);
```

### 更新数据

``` java
sqlExecutor.update(User.builder().age(19).build(), UserCondition.builder().id(id).build());
```

### 删除数据

``` java
sqlExecutor.delete(UserCondition.builder().id(id).build());
```

## 使用`ExecutorProxy`

### Proxy定义和配置

``` java
@ExecutorProxy
public interface UserProxy {
    
    long userCount(UserCondition condition);

    List<User> list(@Param("condition") UserCondition condition);

    User get(@Param("condition") UserCondition condition);
}
```

``` yaml
iface: org.s3s3l.yggdrasil.sample.orm.proxy.UserProxy
methods: 
  - method: userCount
    sql: > 
      select 
        count(*)
      from t_user
        where id in (${arg0.ids?join(", ")})
  - method: list
    sql: >
      select 
        username,
        password
      from t_user
        where id in (${condition.ids?join(", ")})
  # 使用 #变量# 来防止sql注入
  - method: get
    sql: >
      select 
        username,
        password
      from t_user
        where id = #condition.id#
```

### Proxy使用

``` java
// 获取proxy对象
UserProxy userProxy = sqlExecutor.getProxy(UserProxy.class);

log.info(">>>>>>>>>>>>>>>>>> proxy, count for in condition");
System.out.println(userProxy.userCount(UserCondition.builder().ids(new String[] { id, id2 }).build()));

log.info(">>>>>>>>>>>>>>>>>> proxy, list for in condition");
userProxy.list(UserCondition.builder().ids(new String[] { id, id2 }).build()).forEach(System.out::println);

log.info(">>>>>>>>>>>>>>>>>> proxy, get one by id");
System.out.println(userProxy.get(UserCondition.builder().id(id2).build()));
```

## 事务

``` java
log.info(">>>>>>>>>>>>>>>>>> transactional, commit");
sqlExecutor.transactional();
String id3 = StringUtils.getUUIDNoLine();
sqlExecutor.insert(Arrays.asList(
        User.builder()
                .id(id3)
                .username("username3")
                .password("pwd3")
                .realName("realName3")
                .age(18)
                .build()));
sqlExecutor.transactionalCommit();
System.out.println(userProxy.get(UserCondition.builder().id(id3).build()));

log.info(">>>>>>>>>>>>>>>>>> transactional, rollback");
sqlExecutor.transactional();
String id4 = StringUtils.getUUIDNoLine();
sqlExecutor.insert(Arrays.asList(
        User.builder()
                .id(id4)
                .username("username4")
                .password("pwd4")
                .realName("realName4")
                .age(18)
                .build()));
sqlExecutor.rollback();
System.out.println(userProxy.get(UserCondition.builder().id(id4).build()));
```