type FieldType = 'string' | 'number' | 'boolean' | 'object' | 'array'
type Protocol = 'HTTP' | 'RPC' | 'NATIVE'

class Ordered {
    order: number
}

class BaseField {
    /**
     * 名称
     */
    name: string
    /**
     * 类型
     */
    type: FieldType
    /**
     * 是否必须
     */
    required: boolean
    /**
     * 描述
     */
    comment: string
}

/**
 * type === 'object'
 */
class ObjectField extends BaseField {
    subParams: BaseField[]
}

/**
 * type === 'array'
 */
class ArrayField extends BaseField {
    elementField: BaseField
}

class DocNode extends Ordered {
    /**
     * 从父节点到根节点的路径
     * parent, parent`s parent, ..., root
     */
    ancestors: string[]
    /**
     * 主标题
     * @name
     */
    name: string
    /**
     * 作为副标题
     * @literal
     */
    literal: string
    /**
     * 正文内容
     * javadoc正文
     */
    comment: string
    /**
     * 作者
     * @author
     */
    author: string
    /**
     * 过期描述
     * @deprecated
     */
    deprecated: string
    /**
     * 版本号
     * @version
     */
    version: string
    /**
     * 子节点
     */
    children: DocNode[]
    /**
     * 是否是Api详情节点
     */
    isApi: boolean
}

class ApiDocNode extends DocNode {
    /**
     * 接口的协议，默认是{@code NATIVE}
     * @protocol
     */
    protocol: Protocol
    /**
     * 接口地址，默认是{@code ${className}#${methodName}}
     * @path
     */
    path: string
    params: BaseField[]
    response: BaseField[]
}