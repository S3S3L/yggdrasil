``` mermaid
graph LR

Container-->Environment
Container-->Context

subgraph EnvironmentTypes

Environment-.-SystemEnv
Environment-.-ApplicationArgs
Environment-.-JVMArgs
Environment-.-LocalConfigFile
Environment-.-RemoteConfigCenter

end

subgraph BeanCreationStep

BuildInstanceByDefaultConstructor-->BuildInstanceByConstructor-->BuildInstanceByMethod

end

subgraph BeanTypes

Bean-.-Configuration{{Configuration}}===数据对象
Bean-.-Service{{Service}}===业务处理对象
Bean-.-Component{{Component}}===工具对象

end
```