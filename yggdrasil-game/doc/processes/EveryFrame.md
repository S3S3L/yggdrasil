``` mermaid
sequenceDiagram
    Alarm ->> Scene: 触发时间周期(onRing)
    Scene ->> GameObjectManager: 触发重新渲染(doRender)
    Note right of GameObjectManager: 异步
    GameObjectManager ->> GameObjectManager: 执行所有 status == ACTIVE 的GameObject的calculate方法
    GameObjectManager ->> GameObjectManager: 找出所有 status == ACTIVE && isDirty() == true 的GameObject的最小子树
    Note right of GameObjectManager: 异步
    loop 每一个子树根节点
        alt visible == SHOW
            GameObjectManager ->> GameObject: 执行GameObject的preRender方法
            alt children不为空
                loop 每一个子对象
                    GameObject ->> ChildGameObject: 触发子对象的preRender方法
                    ChildGameObject -->> GameObject:
                end
            end 
            GameObject -->> GameObjectManager: 
        end
    end
    Note right of GameObjectManager: 同步
    loop 每一个 status == ACTIVE && visible == SHOW 的GameObject
        GameObjectManager ->> GameObject: 执行GameObject的render方法
        alt children不为空
            loop 每一个子对象
                GameObject ->> ChildGameObject: 触发子对象的render方法
                ChildGameObject -->> GameObject:
            end
        end 
        GameObject -->> GameObjectManager:
    end
    GameObjectManager -->> Scene: 完成重新渲染
    Scene -->> Alarm: 完成场景更新
```