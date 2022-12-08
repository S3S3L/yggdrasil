# yggdrasil-apollo-spring-boot-starter

> 实现在Spring Boot项目中Apollo配置的自动加载

## 使用

### Maven依赖

``` xml
<dependency>
    <groupId>io.github.s3s3l</groupId>
    <artifactId>yggdrasil-apollo-spring-boot-starter</artifactId>
    <version>3.6.1-RELEASE</version>
</dependency>
```

### 注解和配置文件

#### 在application.yml中加入相关配置

``` yaml
yggdrasil:
  apollo:
    enable: true # 组件开关
    app-id: ${your-project-name} # APPID
    docs:
    - name: ${your-apollo-name-space} # 需要加载的Name space
      format: properties # 文件格式，支持： properties | xml | json | yml | yaml
      location:
        target: "" # 目标配置
        type: last # 与目标配置的相对位置，决定配置读取的优先级，越靠前配置优先级越高，支持： before | after | first | last
      field-doc: # 针对properties类型的配置，可以把其中一个字段的内容作为一个文档处理，可空。为空时properties类型的配置将整体作为一个文档处理
      - key: ${your-field-key} # 字段名
        default-key: ${your-default-field-key} # 默认配置字段名，会以较低的优先级去填补key中缺失的字段，可空
        format: yaml # 格式
      processors:
      - ${your-ConfigFileChangedProcessor} # 配置内容发生变化时会按顺序调用，可以通过实现ConfigFileChangedProcessor接口来定制你自己的处理过程
```

#### 启用Spring Boot的自动配置功能

- 加上@EnableAutoConfiguration注解或者使用@SpringBootApplication注解

#### 在Bean中注入属性

``` java
@ApolloConfiguration // 监听Apollo配置变更并触发ConfigFileChangedProcessor，请加上此注解
@Configuration
@ConfigurationProperties(prefix = "your prefix")
public class YourConfiguration {
    private String arg1;
    private String arg2;
}
```

### 在服务中使用

#### 使用注入了属性的Bean

``` java
@Service
public class YourService {
    @Autowired
    private YourConfiguration yourConfiguration;
}
```