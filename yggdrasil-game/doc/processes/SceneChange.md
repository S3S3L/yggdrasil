# 场景切换

## 切换并销毁

- 销毁所有游戏对象
- 销毁所有状态信息
- 切换回来的时候，需重新初始化场景

## 切换至后台

- 对象和状态信息的快照会保存为文件
- 内存充足的情况下，对象和状态信息保留
- 内存不足的情况下，对象和状态信息会被回收
- 切换回来的时候，已经被回收的对象和状态信息可以从文件中恢复
- 切换至后台后，所有的对象都不参与每一帧的计算

``` mermaid

sequenceDiagram

    SceneManager ->> Scene(当前): 触发场景切换
    Scene(当前) ->> GameObjectManager: 构建场景中的游戏对象快照
    loop
        GameObjectManager ->> GameObject(Impl): 构建游戏对象快照(snapshot)
        GameObject(Impl) ->> AbstractGameObject: 构建游戏对象状态信息快照(snapshot)
        loop 
            AbstractGameObject ->> State: 构建游戏对象状态信息快照(StateSnapshot)
            State -->> AbstractGameObject: 
        end
        AbstractGameObject -->> GameObject(Impl): 
        GameObject(Impl) ->> GameObject(Impl): 构建游戏对象属性快照(doSnapshot)
        GameObject(Impl) -->> GameObjectManager: 
    end
    GameObjectManager -->> Scene(当前): 场景中的游戏对象快照构建完成
    Scene(当前) ->> SceneManager: 保存场景自身信息到快照，完成场景快照构建
    SceneManager ->> SnapshotManager: 保存快照到文件，并维护快照索引
    SnapshotManager -->> SceneManager: 保存完成
    SceneManager ->> SceneManager: 更新当前场景状态为（后台）

    SceneManager ->> SnapshotManager: 检索是否存在目标场景的快照信息
    alt 
        SnapshotManager -->> SceneManager: 返回快照信息
        SceneManager ->> Scene(目标): 通过快照恢复场景
        Scene(目标) ->> GameObjectManager: 通过快照恢复游戏对象(restore)
        loop
            GameObjectManager ->> GameObject(Impl): 通过快照恢复游戏对象(restore)
            GameObject(Impl) ->> AbstractGameObject: 通过快照恢复游戏对象(restore)
            loop 
                AbstractGameObject ->> State: 通过快照恢复游戏对象状态
                State -->> AbstractGameObject: 
            end
            AbstractGameObject -->> GameObject(Impl): 
            GameObject(Impl) ->> GameObject(Impl): 恢复游戏对象属性(doRestore)
            GameObject(Impl) -->> GameObjectManager: 
        end
        GameObjectManager -->> Scene(目标): 场景中的游戏对象恢复完成
        Scene(目标) -->> SceneManager: 场景恢复完成
        SceneManager ->> SnapshotManager: 删除快照信息
        SnapshotManager -->> SceneManager: 删除完成
    else
        SnapshotManager -->> SceneManager: 未找到快照信息
        SceneManager ->> Scene(目标): 初始化目标场景
        Scene(目标) -->> SceneManager: 目标场景初始化完成
    end

    SceneManager ->> SceneManager: 更新目标场景状态为（前台）


```