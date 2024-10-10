# yggdrasil-promise

> 实现了类似`JavaScript`的`Promise`的API，可以方便的开启多线程任务，并进行后续处理

## 功能

- 提供了两种实现
  - 基于原生的`lock`实现
  - 基于`Future`实现

## 使用

### Maven依赖

``` xml
<dependency>
    <groupId>io.github.s3s3l</groupId>
    <artifactId>yggdrasil-promise</artifactId>
    <version>3.7.0-RELEASE</version>
</dependency>
```

### 多任务并行

``` java
void allTest() throws InterruptedException, ExecutionException {
    List<TestObject> list = new ArrayList<>(4);
    Promise<Object[]> all = Promise.all(Promise.async(() -> {
        sleep(1000);
        TestObject testObject = TestObject.builder().f1("async1").f2(1).build();
        list.add(testObject);
        return testObject;
    }), Promise.async(() -> {
        sleep(2000);
        TestObject testObject = TestObject.builder().f1("async2").f2(2).build();
        list.add(testObject);
        return testObject;
    }), Promise.async(() -> {
        sleep(500);
        TestObject testObject = TestObject.builder().f1("async3").f2(3).build();
        list.add(testObject);
        return testObject;
    }), Promise.async(() -> {
        sleep(3000);
        TestObject testObject = TestObject.builder().f1("async4").f2(4).build();
        list.add(testObject);
        return testObject;
    }));

    Object[] res = all.get();

    Assertions.assertEquals(1, ((TestObject) res[0]).f2);
    Assertions.assertEquals(2, ((TestObject) res[1]).f2);
    Assertions.assertEquals(3, ((TestObject) res[2]).f2);
    Assertions.assertEquals(4, ((TestObject) res[3]).f2);

    Assertions.assertEquals(3, list.get(0).f2);
    Assertions.assertEquals(1, list.get(1).f2);
    Assertions.assertEquals(2, list.get(2).f2);
    Assertions.assertEquals(4, list.get(3).f2);
}
```

### 开启异步任务，并执行后续处理，最终同步获取结果

``` java
void asyncTest() throws InterruptedException, ExecutionException {
    TestObject testObject = TestObject.builder().f1("async1").f2(1).build();
    Promise<TestObject> async = Promise.async(() -> {
        return testObject;
    }).then((res) -> {
        sleep(1000);
        res.setF2(2);
        return res;
    });
    Assertions.assertEquals(1, testObject.getF2());
    Assertions.assertEquals(2, async.get().getF2());
}
```

### 多个Promise并行

``` java
void twoPromiseTest() throws InterruptedException, ExecutionException {
    List<TestObject> list = new ArrayList<>(2);
    Promise<Void> async1 = Promise.async(() -> {
        sleep(1000);
        return TestObject.builder().f1("async1").f2(1).build();
    }).then((res) -> {
        list.add(res);
    });
    Promise<Void> async2 = Promise.async(() -> {
        sleep(500);
        return TestObject.builder().f1("async2").f2(2).build();
    }).then((res) -> {
        list.add(res);
    });

    async1.get();
    async2.get();
    Assertions.assertEquals(1, list.get(1).getF2());
    Assertions.assertEquals(2, list.get(0).getF2());
}
```

### 切换实现

``` java
// 切换到Future实现
Promise.configure(PromiseImplements.FUTURE);
// 切换到Lock实现
Promise.configure(PromiseImplements.NATIVE_LOCK);
```

> 切换后只会影响后续创建的`Promise`对象