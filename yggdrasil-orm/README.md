# ORM

## Models

### Config

#### Datasource

> DatasourceConfiguration.java

``` java
...
ExecutorConfig executorConfig;
```

> ExecutorConfig.java

``` java
AutoCreateConfig autoCreate; // 自动建表配置
String[] proxyIfacePackages; // proxy 接口所在包名
String[] proxyConfigLocations; // proxy 配置文件所在目录
```

> AutoCreateConfig.java

``` java
boolean enable = false;
boolean force = false;
```

#### Proxy

> ProxyConfig.java

``` java
Class<?> iface;
List<ProxyMethod> methods;
```

> ProxyMethod.java

``` java
String method;
String sql;
```

> ProxyMeta.java

``` java
Class<?> iface;
Map<String, ProxyMethodMeta> methods; // key: methodName 
```

> ProxyMethodMeta.java

``` java
Method method;
Map<String, ParamMeta> params; // key: paramName
```

> ParamMeta.java

``` java
String name;
ParamType type;
Type javaType;
```

### Annotation

> ExecutorProxy.java
