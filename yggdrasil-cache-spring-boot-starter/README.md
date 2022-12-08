# yggdrasil-cache-spring-boot-starter

> 实现在Spring Boot项目中缓存的自动配置和加载

## 功能

- 本地缓存
- Redis缓存
- RedisClient支持
- 混合缓存
- 内容压缩

## 使用

### Maven依赖

``` xml
<dependency>
    <groupId>io.github.s3s3l</groupId>
    <artifactId>yggdrasil-cache-spring-boot-starter</artifactId>
    <version>3.6.1-RELEASE</version>
</dependency>
```

### 缓存配置

``` yaml
yggdrasil:
  cache:
    enable: true # 组件开关
    version-redis: versionRedis # 缓存版本控制Redis，与redis配置中的名称对应，可以与data-redis相同
    data-redis: dataRedis # 数据缓存Redis，与redis配置中的名称对应，可以与version-redis相同
    remote-expire-after-write: 600 # 远程缓存失效时间，单位：秒
    local-expire-after-access: 600 # 本地缓存访问后失效时间，单位：秒
    local-expire-after-write: 600 # 本地缓存写入后失效时间，单位：秒
    local-max-num: 500 # 本地缓存最大数量，该配置为个数，最终缓存占用以单个对象大小×缓存个数为准
    compressor-supplier: io.github.s3s3l.yggdrasil.compress.DefaultZstdCompressSupliers # 压缩器提供者，任意实现Supplier<Compressor>的类全路径
    compress: # 压缩配置
      compress-key: false # 是否对key进行压缩，默认为false
      compress-value: false # 是否对value进行压缩，默认为false
      key-config:
        level: 0 # key的压缩级别，默认为0
      value-config:
        level: 0 # value的压缩级别，默认为0
        ignore-null-value: false # 是否忽略null值，默认为false。当为false时，value为null会抛出错误
```

### 在项目中使用

#### Beans

- io.github.s3s3l.yggdrasil.cache.helper.ComplexCacheHelper<byte[], byte[]>
    - BeanName: complexCacheHelper
- io.github.s3s3l.yggdrasil.cache.interceptor.ComplexCacheInterceptor
    - BeanName: complexCacheInterceptor

#### 构建缓存

``` java
@Service
public class YourService {

    @Resource
    private ComplexCacheHelper<byte[], byte[]> cache;

    public QueryResult query(QueryRequest request) {
        QueryResult data = doSomeThingToGetResult(request);

        byte[] cacheValue = convertDataToByteArray(data);

        byte[] cacheKey = generateCacheKey(request);

        cache.update(cacheKey, cacheValue, "your-cache-scope");

        return data;
    }
}
```

#### 获取缓存

``` java
@Service
public class YourService {

    @Resource
    private ComplexCacheHelper<byte[], byte[]> cache;

    public QueryResult query(QueryRequest request) {
        private String cacheScope = "your-cache-scope";

        byte[] cacheKey = generateCacheKey(request);

        byte[] cacheData = cache.get(cacheKey, cacheScope);

        if (cacheData != null){
            return convertByteArrayToResult(cacheData);
        }

        QueryResult data = doSomeThingToGetResult(request);

        byte[] cacheValue = convertDataToByteArray(data);

        cache.update(cacheKey, cacheValue, cacheScope);

        return data;
    }
}
```

#### 失效缓存

``` java
@Service
public class YourService {

    @Resource
    private ComplexCacheHelper<byte[], byte[]> cache;

    private final String cacheScope = "your-cache-scope";

    public QueryResult query(QueryRequest request) {

        byte[] cacheKey = generateCacheKey(request);

        byte[] cacheData = cache.get(cacheKey, cacheScope);

        if (cacheData != null){
            return convertByteArrayToResult(cacheData);
        }

        QueryResult data = doSomeThingToGetResult(request);

        byte[] cacheValue = convertDataToByteArray(data);

        cache.update(cacheKey, cacheValue, cacheScope);

        return data;
    }

    public ChangeResult change(ChangeRequest request) {
        ChangeResult result = doSomeChange(request);

        cache.expire(cacheScope);

        return result;
    }
}
```