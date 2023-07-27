``` mermaid
graph LR

subgraph 生命周期

加载配置
-->扫描Bean
-->构建依赖树
-->检查循环依赖
-->实例化Bean对象
-->设置属性
-->注入依赖

end
```